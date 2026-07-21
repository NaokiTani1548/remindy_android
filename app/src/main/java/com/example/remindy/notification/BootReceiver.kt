package com.example.remindy.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindy.RemindyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** 端末再起動でアラームは消えるため、Room の内容から全て再スケジュールする。 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val container = (context.applicationContext as RemindyApplication).container
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                container.reminderAlarmScheduler.rescheduleAll(container.reminderRepository.enabledReminders())
                container.studyRepository.currentSetting()?.let { container.studyAlarmScheduler.schedule(it) }
            } finally {
                pending.finish()
            }
        }
    }
}
