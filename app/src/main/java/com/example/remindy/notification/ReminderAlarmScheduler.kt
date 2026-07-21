package com.example.remindy.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.remindy.domain.model.Reminder

class ReminderAlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun schedule(reminder: Reminder) {
        if (!reminder.enabled) { cancel(reminder.id); return }
        val triggerAt = NextFireTimeCalculator.nextTriggerMillis(reminder.schedule) ?: return
        val pending = pendingIntent(reminder.id, reminder.title)
        setExact(triggerAt, pending)
    }

    fun cancel(reminderId: String) {
        alarmManager.cancel(pendingIntent(reminderId, ""))
    }

    fun rescheduleAll(reminders: List<Reminder>) {
        reminders.forEach { schedule(it) }
    }

    private fun setExact(triggerAt: Long, pending: PendingIntent) {
        val canExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
        if (canExact) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        }
    }

    private fun pendingIntent(reminderId: String, title: String): PendingIntent {
        val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
            putExtra(ReminderAlarmReceiver.EXTRA_ID, reminderId)
            putExtra(ReminderAlarmReceiver.EXTRA_TITLE, title)
        }
        return PendingIntent.getBroadcast(
            context, reminderId.hashCode(), intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }
}
