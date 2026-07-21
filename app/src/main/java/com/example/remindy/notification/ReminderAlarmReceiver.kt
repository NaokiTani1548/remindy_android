package com.example.remindy.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindy.RemindyApplication
import com.example.remindy.domain.model.Schedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra(EXTRA_ID) ?: return
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "リマインダー"

        NotificationPublisher.show(context, NotificationChannels.REMINDER, id.hashCode(), title, "通知の時間です")

        // 繰り返し(DAILY/WEEKLY/MONTHLY)は次回を予約し直す。ONE_TIME は一度きり。
        val container = (context.applicationContext as RemindyApplication).container
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reminder = container.reminderRepository
                    .enabledReminders().firstOrNull { it.id == id }
                if (reminder != null && reminder.schedule !is Schedule.OneTime) {
                    container.reminderAlarmScheduler.schedule(reminder)
                }
            } finally {
                pending.finish()
            }
        }
    }

    companion object {
        const val EXTRA_ID = "reminder_id"
        const val EXTRA_TITLE = "reminder_title"
    }
}
