package com.example.remindy.data.repository

import com.example.remindy.data.local.dao.ReminderDao
import com.example.remindy.data.local.dao.SettingDao
import com.example.remindy.data.local.dao.StudyItemDao
import com.example.remindy.data.mapper.toEntity
import com.example.remindy.data.remote.RemindyApi

/** 一括スナップショット取得でローカルを丸ごと更新する。 */
class SyncRepository(
    private val api: RemindyApi,
    private val reminderDao: ReminderDao,
    private val studyItemDao: StudyItemDao,
    private val settingDao: SettingDao,
) {
    suspend fun syncAll() {
        val snapshot = api.sync()
        reminderDao.replaceAll(snapshot.reminders.map { it.toEntity() })
        studyItemDao.replaceAll(snapshot.studyItems.map { it.toEntity() })
        settingDao.upsert(snapshot.studyNotificationSetting.toEntity())
    }
}
