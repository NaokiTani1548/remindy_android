package com.example.remindy.data.repository

import com.example.remindy.data.local.dao.ReminderDao
import com.example.remindy.data.local.dao.SettingDao
import com.example.remindy.data.local.dao.StudyItemDao
import com.example.remindy.data.local.dao.SyncMetadataDao
import com.example.remindy.data.local.entity.SyncMetadataEntity
import com.example.remindy.data.mapper.toEntity
import com.example.remindy.data.mapper.toSyncDto
import com.example.remindy.data.remote.RemindyApi
import com.example.remindy.data.remote.dto.SyncEntityRequest
import com.example.remindy.data.remote.dto.SyncRequestDto
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * デルタ同期リポジトリ。
 * - POST /api/v1/sync で未同期ローカル変更をサーバーに送り、サーバー側の差分を受け取る。
 * - Mutex で同時実行を防ぎ、競合を last-write-wins (updatedAt 比較) で解決する。
 */
class SyncRepository(
    private val api: RemindyApi,
    private val reminderDao: ReminderDao,
    private val studyItemDao: StudyItemDao,
    private val settingDao: SettingDao,
    private val syncMetadataDao: SyncMetadataDao,
) {
    private val mutex = Mutex()

    suspend fun sync() = mutex.withLock {
        val lastSyncedAt = syncMetadataDao.get()?.lastSyncedAt
        val unsyncedReminders = reminderDao.unsynced()
        val unsyncedStudyItems = studyItemDao.unsynced()
        val unsyncedSetting = settingDao.getUnsynced()

        val request = SyncRequestDto(
            lastSyncedAt = lastSyncedAt,
            reminders = SyncEntityRequest(
                upserted = unsyncedReminders.filter { it.deletedAt == null }.map { it.toSyncDto() },
                deleted = unsyncedReminders.filter { it.deletedAt != null }.map { it.toSyncDto() },
            ),
            studyItems = SyncEntityRequest(
                upserted = unsyncedStudyItems.filter { it.deletedAt == null }.map { it.toSyncDto() },
                deleted = unsyncedStudyItems.filter { it.deletedAt != null }.map { it.toSyncDto() },
            ),
            notificationSetting = unsyncedSetting?.toSyncDto(),
        )

        val response = api.sync(request)

        // サーバー応答を適用: ローカルに未同期変更がなければサーバー版を優先
        val serverReminderMap = response.reminders.upserted.associateBy { it.id }
        val serverStudyItemMap = response.studyItems.upserted.associateBy { it.id }

        for (dto in response.reminders.upserted) {
            val local = reminderDao.findById(dto.id)
            if (local == null || local.synced || dto.updatedAt >= local.updatedAt) {
                reminderDao.upsert(dto.toEntity())
            }
        }
        for (dto in response.studyItems.upserted) {
            val local = studyItemDao.findById(dto.id)
            if (local == null || local.synced || dto.updatedAt >= local.updatedAt) {
                studyItemDao.upsert(dto.toEntity())
            }
        }
        response.studyNotificationSetting?.let { dto ->
            val local = settingDao.get()
            if (local == null || local.synced || dto.updatedAt >= local.updatedAt) {
                settingDao.upsert(dto.toEntity())
            }
        }

        // サーバーが返さなかった送信済みレコードを synced = 1 にマーク
        for (entity in unsyncedReminders) {
            if (entity.id !in serverReminderMap) reminderDao.markSynced(entity.id)
        }
        for (entity in unsyncedStudyItems) {
            if (entity.id !in serverStudyItemMap) studyItemDao.markSynced(entity.id)
        }
        if (unsyncedSetting != null && response.studyNotificationSetting == null) {
            settingDao.markSynced()
        }

        // 論理削除済みで同期完了したレコードを物理削除
        for (entity in unsyncedReminders) {
            if (entity.deletedAt != null) {
                val serverVersion = serverReminderMap[entity.id]
                if (serverVersion == null || serverVersion.deletedAt != null) {
                    reminderDao.deleteById(entity.id)
                }
            }
        }
        for (entity in unsyncedStudyItems) {
            if (entity.deletedAt != null) {
                val serverVersion = serverStudyItemMap[entity.id]
                if (serverVersion == null || serverVersion.deletedAt != null) {
                    studyItemDao.deleteById(entity.id)
                }
            }
        }

        syncMetadataDao.upsert(SyncMetadataEntity(lastSyncedAt = response.syncedAt))
    }

    /**
     * バックエンドから全件インポート。
     * ローカルのリマインダー・学習項目を全削除した上で、
     * lastSyncedAt=null で同期することでサーバーの全レコードを取得する。
     */
    suspend fun importFromServer() = mutex.withLock {
        reminderDao.clear()
        studyItemDao.clear()

        val request = SyncRequestDto(
            lastSyncedAt = null,
            reminders = SyncEntityRequest(emptyList(), emptyList()),
            studyItems = SyncEntityRequest(emptyList(), emptyList()),
            notificationSetting = null,
        )
        val response = api.sync(request)

        for (dto in response.reminders.upserted) reminderDao.upsert(dto.toEntity())
        for (dto in response.studyItems.upserted) studyItemDao.upsert(dto.toEntity())
        response.studyNotificationSetting?.let { settingDao.upsert(it.toEntity()) }

        syncMetadataDao.upsert(SyncMetadataEntity(lastSyncedAt = response.syncedAt))
    }
}
