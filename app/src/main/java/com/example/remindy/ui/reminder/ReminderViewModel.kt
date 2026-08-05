package com.example.remindy.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindy.data.repository.ReminderRepository
import com.example.remindy.domain.model.Reminder
import com.example.remindy.domain.model.Schedule
import java.time.LocalDateTime
import com.example.remindy.notification.ReminderAlarmScheduler
import com.example.remindy.ui.common.LoadState
import com.example.remindy.ui.common.debugMessage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val repository: ReminderRepository,
    private val alarmScheduler: ReminderAlarmScheduler,
) : ViewModel() {

    val reminders: StateFlow<List<Reminder>> =
        repository.observeReminders()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow<LoadState>(LoadState.Idle)
    val state: StateFlow<LoadState> = _state.asStateFlow()

    init {
        // 同期・インポートで後からデータが入ってきた場合も含め、
        // reminders が更新されるたびに期限切れ一回通知を自動削除する
        viewModelScope.launch {
            reminders.collect { list ->
                val threshold = LocalDateTime.now().minusDays(2)
                val expired = list.filter { reminder ->
                    val s = reminder.schedule
                    s is Schedule.OneTime &&
                        LocalDateTime.of(s.date, s.time).isBefore(threshold)
                }
                if (expired.isEmpty()) return@collect
                expired.forEach { reminder ->
                    repository.delete(reminder.id)
                    alarmScheduler.cancel(reminder.id)
                }
                alarmScheduler.rescheduleAll(repository.enabledReminders())
            }
        }
        // 初期アラームスケジューリング
        launchGuarded { alarmScheduler.rescheduleAll(repository.enabledReminders()) }
    }

    fun findById(id: String): Reminder? = reminders.value.firstOrNull { it.id == id }

    fun create(title: String, schedule: Schedule) = launchGuarded {
        repository.create(title, schedule)
        rescheduleAll()
    }

    fun update(id: String, title: String, schedule: Schedule) = launchGuarded {
        repository.update(id, title, schedule)
        rescheduleAll()
    }

    fun setEnabled(id: String, enabled: Boolean) = launchGuarded {
        repository.setEnabled(id, enabled)
        if (enabled) findById(id)?.let { alarmScheduler.schedule(it.copy(enabled = true)) }
        else alarmScheduler.cancel(id)
    }

    fun delete(id: String) = launchGuarded {
        repository.delete(id)
        alarmScheduler.cancel(id)
    }

    private suspend fun rescheduleAll() =
        alarmScheduler.rescheduleAll(repository.enabledReminders())

    private fun launchGuarded(block: suspend () -> Unit) {
        _state.value = LoadState.Loading
        viewModelScope.launch {
            try { block(); _state.value = LoadState.Idle }
            catch (e: Exception) { _state.value = LoadState.Error(debugMessage(e)) }
        }
    }
}
