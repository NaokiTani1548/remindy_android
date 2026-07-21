package com.example.remindy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReminderResponseDto(
    val id: String,
    val title: String,
    val schedule: ScheduleDto,
    val enabled: Boolean,
)

@Serializable
data class ReminderListResponseDto(val items: List<ReminderResponseDto>)

@Serializable
data class CreateReminderRequestDto(val title: String, val schedule: ScheduleDto)

@Serializable
data class UpdateReminderRequestDto(val title: String, val schedule: ScheduleDto)

@Serializable
data class ToggleEnabledRequestDto(val enabled: Boolean)
