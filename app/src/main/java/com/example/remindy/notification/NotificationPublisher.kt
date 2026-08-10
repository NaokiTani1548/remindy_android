package com.example.remindy.notification

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.remindy.MainActivity
import android.graphics.Color
import com.example.remindy.R

object NotificationPublisher {

    private val COLOR_REMINDER = Color.parseColor("#1A237E") // Indigo
    private val COLOR_STUDY = Color.parseColor("#512DA8")    // DeepPurple

    fun show(context: Context, channelId: String, notificationId: Int, title: String, text: String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) return

        val openIntent = PendingIntent.getActivity(
            context, notificationId,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val accentColor = when (channelId) {
            NotificationChannels.REMINDER -> COLOR_REMINDER
            NotificationChannels.STUDY -> COLOR_STUDY
            else -> COLOR_REMINDER
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(accentColor)
            .setColorized(true)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setContentIntent(openIntent)
            .build()

        context.getSystemService(NotificationManager::class.java).notify(notificationId, notification)
    }
}
