package com.example.remindy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudyItemResponseDto(
    val id: String,
    val kind: String,
    val prompt: String,
    val answer: String,
    val enabled: Boolean,
    val createdAt: String = "",
    val updatedAt: String = "",
    val deletedAt: String? = null,
)

@Serializable
data class ActiveHoursDto(val start: String, val end: String)

@Serializable
data class NotificationSettingResponseDto(
    val frequency: String,
    val activeHours: ActiveHoursDto,
    val enabled: Boolean,
    val updatedAt: String = "",
)
