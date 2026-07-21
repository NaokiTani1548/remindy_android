package com.example.remindy.notification

import com.example.remindy.domain.model.Schedule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * スケジュール規則から「次回発火時刻」を端末側で計算する（責務境界：サーバは計算しない）。
 * タイムゾーンは JST 固定。過去で終了する ONE_TIME は null（発火不要）。
 */
object NextFireTimeCalculator {
    private val ZONE: ZoneId = ZoneId.of("Asia/Tokyo")

    fun nextTriggerMillis(schedule: Schedule, now: ZonedDateTime = ZonedDateTime.now(ZONE)): Long? {
        return when (schedule) {
            is Schedule.OneTime -> {
                val dt = ZonedDateTime.of(LocalDateTime.of(schedule.date, schedule.time), ZONE)
                if (dt.isAfter(now)) dt.toInstant().toEpochMilli() else null
            }
            is Schedule.Daily -> {
                var dt = ZonedDateTime.of(LocalDateTime.of(now.toLocalDate(), schedule.time), ZONE)
                if (!dt.isAfter(now)) dt = dt.plusDays(1)
                dt.toInstant().toEpochMilli()
            }
            is Schedule.Weekly -> {
                var dt = ZonedDateTime.of(LocalDateTime.of(now.toLocalDate(), schedule.time), ZONE)
                while (dt.dayOfWeek != schedule.dayOfWeek || !dt.isAfter(now)) {
                    dt = dt.plusDays(1)
                }
                dt.toInstant().toEpochMilli()
            }
            is Schedule.Monthly -> {
                var date = firstDateWithDay(now.toLocalDate(), schedule.dayOfMonth)
                var dt = ZonedDateTime.of(LocalDateTime.of(date, schedule.time), ZONE)
                while (!dt.isAfter(now)) {
                    date = firstDateWithDay(date.plusMonths(1).withDayOfMonth(1), schedule.dayOfMonth)
                    dt = ZonedDateTime.of(LocalDateTime.of(date, schedule.time), ZONE)
                }
                dt.toInstant().toEpochMilli()
            }
        }
    }

    /** start 以降で dayOfMonth を持つ最初の日。存在しない月はスキップする。 */
    private fun firstDateWithDay(start: LocalDate, dayOfMonth: Int): LocalDate {
        var monthStart = start.withDayOfMonth(1)
        repeat(12) {
            if (dayOfMonth <= monthStart.lengthOfMonth()) {
                val candidate = monthStart.withDayOfMonth(dayOfMonth)
                if (!candidate.isBefore(start)) return candidate
            }
            monthStart = monthStart.plusMonths(1)
        }
        return start.withDayOfMonth(minOf(dayOfMonth, start.lengthOfMonth()))
    }
}
