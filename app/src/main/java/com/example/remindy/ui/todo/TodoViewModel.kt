package com.example.remindy.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindy.data.repository.TodoRepository
import com.example.remindy.domain.model.Todo
import com.example.remindy.ui.common.LoadState
import com.example.remindy.ui.common.debugMessage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    val items: StateFlow<List<Todo>> =
        repository.observeAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow<LoadState>(LoadState.Idle)
    val state: StateFlow<LoadState> = _state.asStateFlow()

    fun findById(id: String): Todo? = items.value.firstOrNull { it.id == id }

    fun create(title: String, description: String) =
        launchGuarded { repository.create(title, description) }

    fun update(id: String, title: String, description: String) =
        launchGuarded { repository.update(id, title, description) }

    fun delete(id: String) = launchGuarded { repository.delete(id) }

    private fun launchGuarded(block: suspend () -> Unit) {
        _state.value = LoadState.Loading
        viewModelScope.launch {
            try { block(); _state.value = LoadState.Idle }
            catch (e: Exception) { _state.value = LoadState.Error(debugMessage(e)) }
        }
    }
}
