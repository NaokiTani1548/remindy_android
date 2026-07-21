package com.example.remindy.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// バックエンドの判別共用体 {"type": "...", ...} に一致させる。
// time/date/dayOfWeek は文字列でやり取りし、java.time への変換は mapper 側で行う。
@Serializable
sealed interface ScheduleDto {
    @Serializable
    @SerialName("ONE_TIME")
    data class OneTime(val date: String, val time: String) : ScheduleDto

    @Serializable
    @SerialName("DAILY")
    data class Daily(val time: String) : ScheduleDto

    @Serializable
    @SerialName("WEEKLY")
    data class Weekly(val dayOfWeek: String, val time: String) : ScheduleDto

    @Serializable
    @SerialName("MONTHLY")
    data class Monthly(val dayOfMonth: Int, val time: String) : ScheduleDto
}
