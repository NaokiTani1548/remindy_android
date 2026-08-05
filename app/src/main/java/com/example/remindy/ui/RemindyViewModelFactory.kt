package com.example.remindy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.remindy.di.AppContainer
import com.example.remindy.ui.auth.AuthViewModel
import com.example.remindy.ui.connection.ConnectionViewModel
import com.example.remindy.ui.reminder.ReminderViewModel
import com.example.remindy.ui.settings.SettingsViewModel
import com.example.remindy.ui.study.StudyViewModel

@Suppress("UNCHECKED_CAST")
class RemindyViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) ->
            AuthViewModel(container.authRepository) as T
        modelClass.isAssignableFrom(ConnectionViewModel::class.java) ->
            ConnectionViewModel(container.healthRepository) as T
        modelClass.isAssignableFrom(ReminderViewModel::class.java) ->
            ReminderViewModel(container.reminderRepository, container.reminderAlarmScheduler) as T
        modelClass.isAssignableFrom(StudyViewModel::class.java) ->
            StudyViewModel(container.studyRepository) as T
        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(container.studyRepository, container.syncRepository, container.studyAlarmScheduler) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
