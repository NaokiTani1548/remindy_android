package com.example.remindy.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationChannels {
    const val REMINDER = "reminder_channel"
    const val STUDY = "study_channel"

    fun createAll(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(REMINDER, "リマインダー", NotificationManager.IMPORTANCE_HIGH)
        )
        manager.createNotificationChannel(
            NotificationChannel(STUDY, "単語学習", NotificationManager.IMPORTANCE_DEFAULT)
        )
    }
}
