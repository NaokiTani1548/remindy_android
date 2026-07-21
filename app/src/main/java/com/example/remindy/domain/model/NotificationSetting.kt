package com.example.remindy.domain.model

enum class Frequency { ONCE, THREE_TIMES, FIVE_TIMES }

data class NotificationSetting(
    val frequency: Frequency,
    val enabled: Boolean,
) {
    companion object {
        const val ACTIVE_HOURS_START = 9
        const val ACTIVE_HOURS_END = 21
    }
}
