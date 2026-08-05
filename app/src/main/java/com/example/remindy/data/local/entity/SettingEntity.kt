package com.example.remindy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_setting")
data class SettingEntity(
    @PrimaryKey val id: Int = 0,
    val frequency: String,
    val enabled: Boolean,
    val updatedAt: String,
    val synced: Boolean,
)
