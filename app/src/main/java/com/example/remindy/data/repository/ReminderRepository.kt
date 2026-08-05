package com.example.remindy.data.repository

import com.example.remindy.data.local.dao.ReminderDao
import com.example.remindy.data.local.entity.ReminderEntity
import com.example.remindy.data.mapper.toDomain
import com.example.remindy.domain.model.Reminder
import com.example.remindy.domain.model.Schedule
import com.example.remindy.util.UuidV7
import com.example.remindy.util.nowUtc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter

private val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss")

class ReminderRepository(private val dao: ReminderDao) {

    fun observeReminders(): Flow<List<Reminder>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun allReminders(): List<Reminder> = dao.allActive().map { it.toDomain() }

    suspend fun enabledReminders(): List<Reminder> = dao.enabled().map { it.toDomain() }

    suspend fun create(title: String, schedule: Schedule) {
        val now = nowUtc()
        dao.upsert(schedule.toEntity(
            id = UuidV7.generate(),
            title = title,
            enabled = true,
            createdAt = now,
            updatedAt = now,
        ))
    }

    suspend fun update(id: String, title: String, schedule: Schedule) {
        val existing = dao.findById(id) ?: return
        dao.upsert(schedule.toEntity(
            id = id,
            title = title,
            enabled = existing.enabled,
            createdAt = existing.createdAt,
            updatedAt = nowUtc(),
        ))
    }

    suspend fun setEnabled(id: String, enabled: Boolean) {
        val existing = dao.findById(id) ?: return
        dao.upsert(existing.copy(enabled = enabled, updatedAt = nowUtc(), synced = false))
    }

    suspend fun delete(id: String) {
        val existing = dao.findById(id) ?: return
        val now = nowUtc()
        dao.upsert(existing.copy(deletedAt = now, updatedAt = now, synced = false))
    }
}

private fun Schedule.toEntity(
    id: String,
    title: String,
    enabled: Boolean,
    createdAt: String,
    updatedAt: String,
) = ReminderEntity(
    id = id,
    title = title,
    scheduleType = when (this) {
        is Schedule.OneTime -> "ONE_TIME"
        is Schedule.Daily -> "DAILY"
        is Schedule.Weekly -> "WEEKLY"
        is Schedule.Monthly -> "MONTHLY"
    },
    scheduleTime = when (this) {
        is Schedule.OneTime -> time.format(TIME_FORMAT)
        is Schedule.Daily -> time.format(TIME_FORMAT)
        is Schedule.Weekly -> time.format(TIME_FORMAT)
        is Schedule.Monthly -> time.format(TIME_FORMAT)
    },
    scheduleDate = (this as? Schedule.OneTime)?.date?.toString(),
    scheduleDayOfWeek = (this as? Schedule.Weekly)?.dayOfWeek?.name,
    scheduleDayOfMonth = (this as? Schedule.Monthly)?.dayOfMonth,
    enabled = enabled,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = null,
    synced = false,
)
