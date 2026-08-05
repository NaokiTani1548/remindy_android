package com.example.remindy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SyncEntityRequest<T>(
    val upserted: List<T>,
    val deleted: List<T>,
)

/** POST /api/v1/sync リクエスト */
@Serializable
data class SyncRequestDto(
    val lastSyncedAt: String?,
    val reminders: SyncEntityRequest<ReminderSyncItemDto>,
    val studyItems: SyncEntityRequest<StudyItemSyncItemDto>,
    val notificationSetting: NotificationSettingSyncDto?,
)

@Serializable
data class ReminderSyncItemDto(
    val id: String,
    val title: String,
    val schedule: ScheduleDto,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)

@Serializable
data class StudyItemSyncItemDto(
    val id: String,
    val kind: String,
    val prompt: String,
    val answer: String,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)

@Serializable
data class NotificationSettingSyncDto(
    val frequency: String,
    val enabled: Boolean,
    val updatedAt: String,
)

/** POST /api/v1/sync レスポンス */
@Serializable
data class SyncResponseDto(
    val syncedAt: String,
    val reminders: SyncEntityRequest<ReminderResponseDto>,
    val studyItems: SyncEntityRequest<StudyItemResponseDto>,
    val studyNotificationSetting: NotificationSettingResponseDto?,
)
