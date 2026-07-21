package com.example.remindy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val scheduleType: String,
    val scheduleTime: String,
    val scheduleDate: String?,
    val scheduleDayOfWeek: String?,
    val scheduleDayOfMonth: Int?,
    val enabled: Boolean,
)
