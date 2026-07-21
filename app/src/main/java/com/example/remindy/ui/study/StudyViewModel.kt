package com.example.remindy.ui.study

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindy.data.repository.StudyRepository
import com.example.remindy.domain.model.StudyItem
import com.example.remindy.domain.model.StudyItemKind
import com.example.remindy.ui.common.LoadState
import com.example.remindy.ui.common.debugMessage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StudyViewModel(private val repository: StudyRepository) : ViewModel() {

    val items: StateFlow<List<StudyItem>> =
        repository.observeItems()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow<LoadState>(LoadState.Idle)
    val state: StateFlow<LoadState> = _state.asStateFlow()

    init { refresh() }

    fun refresh() = launchGuarded { repository.refreshItems() }

    fun findById(id: String): StudyItem? = items.value.firstOrNull { it.id == id }

    fun create(kind: StudyItemKind, prompt: String, answer: String) =
        launchGuarded { repository.create(kind, prompt, answer) }

    fun update(id: String, kind: StudyItemKind, prompt: String, answer: String) =
        launchGuarded { repository.update(id, kind, prompt, answer) }

    fun setEnabled(id: String, enabled: Boolean) = launchGuarded { repository.setEnabled(id, enabled) }

    fun delete(id: String) = launchGuarded { repository.delete(id) }

    private fun launchGuarded(block: suspend () -> Unit) {
        _state.value = LoadState.Loading
        viewModelScope.launch {
            try { block(); _state.value = LoadState.Idle }
            catch (e: Exception) { _state.value = LoadState.Error(debugMessage(e)) }
        }
    }
}
