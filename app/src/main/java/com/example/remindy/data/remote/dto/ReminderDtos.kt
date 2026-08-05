package com.example.remindy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReminderResponseDto(
    val id: String,
    val title: String,
    val schedule: ScheduleDto,
    val enabled: Boolean,
    val createdAt: String = "",
    val updatedAt: String = "",
    val deletedAt: String? = null,
)
