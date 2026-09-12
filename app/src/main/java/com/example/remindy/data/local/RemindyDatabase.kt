package com.example.remindy.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.remindy.data.local.dao.ReminderDao
import com.example.remindy.data.local.dao.SettingDao
import com.example.remindy.data.local.dao.StudyItemDao
import com.example.remindy.data.local.dao.SyncMetadataDao
import com.example.remindy.data.local.dao.TodoDao
import com.example.remindy.data.local.entity.ReminderEntity
import com.example.remindy.data.local.entity.SettingEntity
import com.example.remindy.data.local.entity.StudyItemEntity
import com.example.remindy.data.local.entity.SyncMetadataEntity
import com.example.remindy.data.local.entity.TodoEntity

@Database(
    entities = [
        ReminderEntity::class,
        StudyItemEntity::class,
        SettingEntity::class,
        SyncMetadataEntity::class,
        TodoEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
abstract class RemindyDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun studyItemDao(): StudyItemDao
    abstract fun settingDao(): SettingDao
    abstract fun syncMetadataDao(): SyncMetadataDao
    abstract fun todoDao(): TodoDao
}
