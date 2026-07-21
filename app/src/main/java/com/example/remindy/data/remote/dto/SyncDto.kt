package com.example.remindy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SyncResponseDto(
    val reminders: List<ReminderResponseDto>,
    val studyItems: List<StudyItemResponseDto>,
    val studyNotificationSetting: NotificationSettingResponseDto,
    val serverTime: String,
)
