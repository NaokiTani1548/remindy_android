package com.example.remindy.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindy.data.repository.StudyRepository
import com.example.remindy.data.repository.SyncRepository
import com.example.remindy.domain.model.Frequency
import com.example.remindy.domain.model.NotificationSetting
import com.example.remindy.notification.StudyAlarmScheduler
import com.example.remindy.ui.common.LoadState
import com.example.remindy.ui.common.debugMessage
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: StudyRepository,
    private val syncRepository: SyncRepository,
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
                // DBに設定がなければデフォルトを挿入（初回起動時 / サーバー未同期時でも画面を表示できるように）
                if (repository.currentSetting() == null) {
                    repository.updateSetting(Frequency.THREE_TIMES, enabled = true)
                }
                repository.currentSetting()?.let { studyAlarmScheduler.schedule(it) }
            } catch (_: Exception) { }
        }
    }

    fun importFromBackend() {
        _state.value = LoadState.Loading
        viewModelScope.launch {
            try {
                syncRepository.importFromServer()
                _state.value = LoadState.Idle
            } catch (e: Exception) {
                val msg = if (e is HttpException && e.code() == 401) {
                    "認証の有効期限が切れました。再ログインしてください。"
                } else {
                    debugMessage(e)
                }
                _state.value = LoadState.Error(msg)
            }
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
