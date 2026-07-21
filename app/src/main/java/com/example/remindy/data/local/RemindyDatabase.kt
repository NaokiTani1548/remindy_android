package com.example.remindy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.remindy.data.local.dao.ReminderDao
import com.example.remindy.data.local.dao.SettingDao
import com.example.remindy.data.local.dao.StudyItemDao
import com.example.remindy.data.local.entity.ReminderEntity
import com.example.remindy.data.local.entity.SettingEntity
import com.example.remindy.data.local.entity.StudyItemEntity

@Database(
    entities = [ReminderEntity::class, StudyItemEntity::class, SettingEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class RemindyDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun studyItemDao(): StudyItemDao
    abstract fun settingDao(): SettingDao
}
