package com.example.remindy.data.repository

import com.example.remindy.data.local.dao.SettingDao
import com.example.remindy.data.local.dao.StudyItemDao
import com.example.remindy.data.local.entity.SettingEntity
import com.example.remindy.data.local.entity.StudyItemEntity
import com.example.remindy.data.mapper.toDomain
import com.example.remindy.domain.model.Frequency
import com.example.remindy.domain.model.NotificationSetting
import com.example.remindy.domain.model.StudyItem
import com.example.remindy.domain.model.StudyItemKind
import com.example.remindy.util.UuidV7
import com.example.remindy.util.nowUtc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudyRepository(
    private val itemDao: StudyItemDao,
    private val settingDao: SettingDao,
) {
    fun observeItems(): Flow<List<StudyItem>> =
        itemDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeSetting(): Flow<NotificationSetting?> =
        settingDao.observe().map { it?.toDomain() }

    suspend fun enabledItems(): List<StudyItem> = itemDao.enabled().map { it.toDomain() }

    suspend fun currentSetting(): NotificationSetting? = settingDao.get()?.toDomain()

    suspend fun create(kind: StudyItemKind, prompt: String, answer: String) {
        val now = nowUtc()
        itemDao.upsert(StudyItemEntity(
            id = UuidV7.generate(),
            kind = kind.name,
            prompt = prompt,
            answer = answer,
            enabled = true,
            createdAt = now,
            updatedAt = now,
            deletedAt = null,
            synced = false,
        ))
    }

    suspend fun update(id: String, kind: StudyItemKind, prompt: String, answer: String) {
        val existing = itemDao.findById(id) ?: return
        itemDao.upsert(existing.copy(
            kind = kind.name,
            prompt = prompt,
            answer = answer,
            updatedAt = nowUtc(),
            synced = false,
        ))
    }

    suspend fun setEnabled(id: String, enabled: Boolean) {
        val existing = itemDao.findById(id) ?: return
        itemDao.upsert(existing.copy(enabled = enabled, updatedAt = nowUtc(), synced = false))
    }

    suspend fun delete(id: String) {
        val existing = itemDao.findById(id) ?: return
        val now = nowUtc()
        itemDao.upsert(existing.copy(deletedAt = now, updatedAt = now, synced = false))
    }

    suspend fun updateSetting(frequency: Frequency, enabled: Boolean) {
        settingDao.upsert(SettingEntity(
            frequency = frequency.name,
            enabled = enabled,
            updatedAt = nowUtc(),
            synced = false,
        ))
    }
}
