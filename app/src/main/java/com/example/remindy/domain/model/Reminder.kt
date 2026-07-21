package com.example.remindy.domain.model

data class Reminder(
    val id: String,
    val title: String,
    val schedule: Schedule,
    val enabled: Boolean,
)
