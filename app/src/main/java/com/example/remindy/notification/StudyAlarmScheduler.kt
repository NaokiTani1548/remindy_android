package com.example.remindy.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.remindy.domain.model.Frequency
import com.example.remindy.domain.model.NotificationSetting
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * 学習通知を頻度に応じて 9:00–21:00 に均等配置し、各時刻に日次アラームを予約する。
 * 実際に「どの項目を出すか」の選出は発火時（StudyAlarmReceiver）に端末側で行う。
 */
class StudyAlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val zone = ZoneId.of("Asia/Tokyo")

    fun schedule(setting: NotificationSetting) {
        cancelAll()
        if (!setting.enabled) return
        timesFor(setting.frequency).forEachIndexed { index, time ->
            val triggerAt = nextDailyTrigger(time)
            setExact(triggerAt, pendingIntent(index))
        }
    }

    fun cancelAll() {
        repeat(MAX_SLOTS) { alarmManager.cancel(pendingIntent(it)) }
    }

    private fun timesFor(frequency: Frequency): List<LocalTime> {
        val start = NotificationSetting.ACTIVE_HOURS_START   // 9
        val end = NotificationSetting.ACTIVE_HOURS_END       // 21
        val count = when (frequency) {
            Frequency.ONCE -> 1
            Frequency.THREE_TIMES -> 3
            Frequency.FIVE_TIMES -> 5
        }
        if (count == 1) return listOf(LocalTime.of((start + end) / 2, 0))
        val step = (end - start).toDouble() / (count - 1)
        return (0 until count).map { i -> LocalTime.of((start + step * i).toInt(), 0) }
    }

    private fun nextDailyTrigger(time: LocalTime): Long {
        val now = ZonedDateTime.now(zone)
        var dt = ZonedDateTime.of(LocalDateTime.of(now.toLocalDate(), time), zone)
        if (!dt.isAfter(now)) dt = dt.plusDays(1)
        return dt.toInstant().toEpochMilli()
    }

    private fun setExact(triggerAt: Long, pending: PendingIntent) {
        val canExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
        if (canExact) alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        else alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
    }

    private fun pendingIntent(slot: Int): PendingIntent {
        val intent = Intent(context, StudyAlarmReceiver::class.java).apply {
            putExtra(StudyAlarmReceiver.EXTRA_SLOT, slot)
        }
        return PendingIntent.getBroadcast(
            context, STUDY_REQUEST_BASE + slot, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    companion object {
        private const val MAX_SLOTS = 5
        private const val STUDY_REQUEST_BASE = 900000
    }
}
