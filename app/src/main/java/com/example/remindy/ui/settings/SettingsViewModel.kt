package com.example.remindy.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindy.data.repository.StudyRepository
import com.example.remindy.domain.model.Frequency
import com.example.remindy.domain.model.NotificationSetting
import com.example.remindy.notification.StudyAlarmScheduler
import com.example.remindy.ui.common.LoadState
import com.example.remindy.ui.common.debugMessage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: StudyRepository,
    private val studyAlarmScheduler: StudyAlarmScheduler,
) : ViewModel() {

    val setting: StateFlow<NotificationSetting?> =
        repository.observeSetting()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _state = MutableStateFlow<LoadState>(LoadState.Idle)
    val state: StateFlow<LoadState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.refreshSetting()
                repository.currentSetting()?.let { studyAlarmScheduler.schedule(it) }
            } catch (_: Exception) { }
        }
    }

    fun update(frequency: Frequency, enabled: Boolean) {
        _state.value = LoadState.Loading
        viewModelScope.launch {
            try {
                repository.updateSetting(frequency, enabled)
                repository.currentSetting()?.let { studyAlarmScheduler.schedule(it) }
                _state.value = LoadState.Idle
            } catch (e: Exception) {
                _state.value = LoadState.Error(debugMessage(e))
            }
        }
    }
}
