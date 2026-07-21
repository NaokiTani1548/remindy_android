package com.example.remindy.data.mapper

import com.example.remindy.data.local.entity.ReminderEntity
import com.example.remindy.data.local.entity.SettingEntity
import com.example.remindy.data.local.entity.StudyItemEntity
import com.example.remindy.data.remote.dto.*
import com.example.remindy.domain.model.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

// ----- Reminder -----
fun ReminderResponseDto.toEntity(): ReminderEntity {
    val s = schedule
    return ReminderEntity(
        id = id,
        title = title,
        scheduleType = when (s) {
            is ScheduleDto.OneTime -> "ONE_TIME"
            is ScheduleDto.Daily -> "DAILY"
            is ScheduleDto.Weekly -> "WEEKLY"
            is ScheduleDto.Monthly -> "MONTHLY"
        },
        scheduleTime = s.timeValue(),
        scheduleDate = (s as? ScheduleDto.OneTime)?.date,
        scheduleDayOfWeek = (s as? ScheduleDto.Weekly)?.dayOfWeek,
        scheduleDayOfMonth = (s as? ScheduleDto.Monthly)?.dayOfMonth,
        enabled = enabled,
    )
}

private fun ScheduleDto.timeValue(): String = when (this) {
    is ScheduleDto.OneTime -> time
    is ScheduleDto.Daily -> time
    is ScheduleDto.Weekly -> time
    is ScheduleDto.Monthly -> time
}

fun ReminderEntity.toDomain(): Reminder {
    val time = LocalTime.parse(scheduleTime)
    val schedule: Schedule = when (scheduleType) {
        "ONE_TIME" -> Schedule.OneTime(LocalDate.parse(scheduleDate), time)
        "DAILY" -> Schedule.Daily(time)
        "WEEKLY" -> Schedule.Weekly(DayOfWeek.valueOf(scheduleDayOfWeek!!), time)
        "MONTHLY" -> Schedule.Monthly(scheduleDayOfMonth!!, time)
        else -> Schedule.Daily(time)
    }
    return Reminder(id = id, title = title, schedule = schedule, enabled = enabled)
}

// ----- StudyItem -----
fun StudyItemResponseDto.toEntity() = StudyItemEntity(id, kind, prompt, answer, enabled)

fun StudyItemEntity.toDomain() = StudyItem(
    id = id,
    kind = StudyItemKind.valueOf(kind),
    prompt = prompt,
    answer = answer,
    enabled = enabled,
)

// ----- Setting -----
fun NotificationSettingResponseDto.toEntity() = SettingEntity(frequency = frequency, enabled = enabled)

fun SettingEntity.toDomain() = NotificationSetting(
    frequency = Frequency.valueOf(frequency),
    enabled = enabled,
)
