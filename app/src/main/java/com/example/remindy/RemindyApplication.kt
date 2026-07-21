package com.example.remindy

import android.app.Application
import com.example.remindy.di.AppContainer
import com.example.remindy.notification.NotificationChannels

class RemindyApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        NotificationChannels.createAll(this)
    }
}
