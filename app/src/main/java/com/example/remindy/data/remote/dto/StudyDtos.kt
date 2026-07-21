package com.example.remindy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyItemResponseDto(
    val id: String,
    val kind: String,
    val prompt: String,
    val answer: String,
    val enabled: Boolean,
)

@Serializable
data class StudyItemListResponseDto(val items: List<StudyItemResponseDto>)

@Serializable
data class CreateStudyItemRequestDto(val kind: String, val prompt: String, val answer: String)

@Serializable
data class UpdateStudyItemRequestDto(val kind: String, val prompt: String, val answer: String)

@Serializable
data class ToggleStudyItemRequestDto(val enabled: Boolean)

@Serializable
data class ActiveHoursDto(val start: String, val end: String)

@Serializable
data class NotificationSettingResponseDto(
    val frequency: String,
    val activeHours: ActiveHoursDto,
    val enabled: Boolean,
)

@Serializable
data class UpdateNotificationSettingRequestDto(val frequency: String, val enabled: Boolean)
