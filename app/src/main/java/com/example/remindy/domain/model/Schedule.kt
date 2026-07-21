package com.example.remindy.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

sealed interface Schedule {
    val time: LocalTime
    data class OneTime(val date: LocalDate, override val time: LocalTime) : Schedule
    data class Daily(override val time: LocalTime) : Schedule
    data class Weekly(val dayOfWeek: DayOfWeek, override val time: LocalTime) : Schedule
    data class Monthly(val dayOfMonth: Int, override val time: LocalTime) : Schedule
}
