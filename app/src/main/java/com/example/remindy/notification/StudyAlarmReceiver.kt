package com.example.remindy.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindy.RemindyApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 学習通知の発火。責務境界どおり「有効な学習項目からランダムに1件を端末側で選出」し、
 * 通知には表(prompt)のみ表示（裏はアプリを開いて確認）。翌日の同スロットを再予約する。
 */
class StudyAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val slot = intent.getIntExtra(EXTRA_SLOT, 0)
        val container = (context.applicationContext as RemindyApplication).container
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val setting = container.studyRepository.currentSetting()
                if (setting == null || !setting.enabled) return@launch
                val items = container.studyRepository.enabledItems()
                if (items.isNotEmpty()) {
                    val chosen = items.random()
                    NotificationPublisher.show(
                        context, NotificationChannels.STUDY,
                        NOTIF_BASE + slot, chosen.prompt, "タップして答えを確認",
                    )
                }
                // 翌日の同時刻へ再予約
                container.studyAlarmScheduler.schedule(setting)
            } finally {
                pending.finish()
            }
        }
    }

    companion object {
        const val EXTRA_SLOT = "study_slot"
        private const val NOTIF_BASE = 800000
    }
}
