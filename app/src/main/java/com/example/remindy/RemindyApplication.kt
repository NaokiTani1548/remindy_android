package com.example.remindy

import android.app.Application
import androidx.work.*
import com.example.remindy.di.AppContainer
import com.example.remindy.notification.NotificationChannels
import com.example.remindy.sync.SyncWorker
import java.util.concurrent.TimeUnit

class RemindyApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        NotificationChannels.createAll(this)
        scheduleSyncWork()
    }

    private fun scheduleSyncWork() {
        val workManager = WorkManager.getInstance(this)
        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // 起動時に即時同期
        workManager.enqueue(
            OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(networkConstraint)
                .build()
        )

        // 15分ごとの定期同期
        workManager.enqueueUniquePeriodicWork(
            "remindy_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(networkConstraint)
                .build()
        )
    }
}
