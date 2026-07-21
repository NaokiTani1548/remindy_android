package com.example.remindy.data.repository

import com.example.remindy.data.local.dao.ReminderDao
import com.example.remindy.data.mapper.*
import com.example.remindy.data.remote.RemindyApi
import com.example.remindy.data.remote.dto.*
import com.example.remindy.domain.model.Reminder
import com.example.remindy.domain.model.Schedule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepository(
    private val api: RemindyApi,
    private val dao: ReminderDao,
) {
    fun observeReminders(): Flow<List<Reminder>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun enabledReminders(): List<Reminder> = dao.enabled().map { it.toDomain() }

    suspend fun refresh() {
        val remote = api.listReminders().items
        dao.replaceAll(remote.map { it.toEntity() })
    }

    suspend fun create(title: String, schedule: Schedule) {
        val created = api.createReminder(CreateReminderRequestDto(title, schedule.toDto()))
        dao.upsert(created.toEntity())
    }

    suspend fun update(id: String, title: String, schedule: Schedule) {
        val updated = api.updateReminder(id, UpdateReminderRequestDto(title, schedule.toDto()))
        dao.upsert(updated.toEntity())
    }

    suspend fun setEnabled(id: String, enabled: Boolean) {
        val updated = api.toggleReminder(id, ToggleEnabledRequestDto(enabled))
        dao.upsert(updated.toEntity())
    }

    suspend fun delete(id: String) {
        api.deleteReminder(id)
        dao.deleteById(id)
    }
}
