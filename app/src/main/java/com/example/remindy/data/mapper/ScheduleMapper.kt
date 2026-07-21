package com.example.remindy.data.mapper

import com.example.remindy.data.remote.dto.ScheduleDto
import com.example.remindy.domain.model.Schedule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

fun ScheduleDto.toDomain(): Schedule = when (this) {
    is ScheduleDto.OneTime -> Schedule.OneTime(LocalDate.parse(date), LocalTime.parse(time))
    is ScheduleDto.Daily -> Schedule.Daily(LocalTime.parse(time))
    is ScheduleDto.Weekly -> Schedule.Weekly(DayOfWeek.valueOf(dayOfWeek), LocalTime.parse(time))
    is ScheduleDto.Monthly -> Schedule.Monthly(dayOfMonth, LocalTime.parse(time))
}

fun Schedule.toDto(): ScheduleDto = when (this) {
    is Schedule.OneTime -> ScheduleDto.OneTime(date.toString(), time.toString())
    is Schedule.Daily -> ScheduleDto.Daily(time.toString())
    is Schedule.Weekly -> ScheduleDto.Weekly(dayOfWeek.name, time.toString())
    is Schedule.Monthly -> ScheduleDto.Monthly(dayOfMonth, time.toString())
}
