package com.example.remindy.data.repository

import com.example.remindy.data.local.dao.SettingDao
import com.example.remindy.data.local.dao.StudyItemDao
import com.example.remindy.data.local.entity.SettingEntity
import com.example.remindy.data.mapper.*
import com.example.remindy.data.remote.RemindyApi
import com.example.remindy.data.remote.dto.*
import com.example.remindy.domain.model.Frequency
import com.example.remindy.domain.model.NotificationSetting
import com.example.remindy.domain.model.StudyItem
import com.example.remindy.domain.model.StudyItemKind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudyRepository(
    private val api: RemindyApi,
    private val itemDao: StudyItemDao,
    private val settingDao: SettingDao,
) {
    fun observeItems(): Flow<List<StudyItem>> =
        itemDao.observeAll().map { list -> list.map { it.toDomain() } }

    fun observeSetting(): Flow<NotificationSetting?> =
        settingDao.observe().map { it?.toDomain() }

    suspend fun enabledItems(): List<StudyItem> = itemDao.enabled().map { it.toDomain() }

    suspend fun currentSetting(): NotificationSetting? = settingDao.get()?.toDomain()

    suspend fun refreshItems() {
        val remote = api.listStudyItems().items
        itemDao.replaceAll(remote.map { it.toEntity() })
    }

    suspend fun refreshSetting() {
        val remote = api.getNotificationSetting()
        settingDao.upsert(remote.toEntity())
    }

    suspend fun create(kind: StudyItemKind, prompt: String, answer: String) {
        val created = api.createStudyItem(CreateStudyItemRequestDto(kind.name, prompt, answer))
        itemDao.upsert(created.toEntity())
    }

    suspend fun update(id: String, kind: StudyItemKind, prompt: String, answer: String) {
        val updated = api.updateStudyItem(id, UpdateStudyItemRequestDto(kind.name, prompt, answer))
        itemDao.upsert(updated.toEntity())
    }

    suspend fun setEnabled(id: String, enabled: Boolean) {
        val updated = api.toggleStudyItem(id, ToggleStudyItemRequestDto(enabled))
        itemDao.upsert(updated.toEntity())
    }

    suspend fun delete(id: String) {
        api.deleteStudyItem(id)
        itemDao.deleteById(id)
    }

    suspend fun updateSetting(frequency: Frequency, enabled: Boolean) {
        val updated = api.updateNotificationSetting(UpdateNotificationSettingRequestDto(frequency.name, enabled))
        settingDao.upsert(updated.toEntity())
    }
}
