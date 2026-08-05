package com.example.remindy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val scheduleType: String,       // ONE_TIME | DAILY | WEEKLY | MONTHLY
    val scheduleTime: String,       // HH:mm
    val scheduleDate: String?,      // yyyy-MM-dd (ONE_TIME only)
    val scheduleDayOfWeek: String?, // MONDAY etc. (WEEKLY only)
    val scheduleDayOfMonth: Int?,   // 1-31 (MONTHLY only)
    val enabled: Boolean,
    val createdAt: String,          // ISO 8601 UTC
    val updatedAt: String,          // ISO 8601 UTC
    val deletedAt: String?,         // null = active, non-null = soft-deleted
    val synced: Boolean,            // false = has unsynced local changes
)
