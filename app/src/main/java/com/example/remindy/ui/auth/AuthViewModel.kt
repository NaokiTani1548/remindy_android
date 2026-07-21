package com.example.remindy.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remindy.data.repository.AuthRepository
import com.example.remindy.ui.common.LoadState
import com.example.remindy.ui.common.debugMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow<LoadState>(LoadState.Idle)
    val state: StateFlow<LoadState> = _state.asStateFlow()

    fun login(email: String, password: String) = run(email, password, register = false)
    fun register(email: String, password: String) = run(email, password, register = true)

    private fun run(email: String, password: String, register: Boolean) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = LoadState.Error("メールアドレスとパスワードを入力してください")
            return
        }
        _state.value = LoadState.Loading
        viewModelScope.launch {
            try {
                if (register) {
                    authRepository.register(email, password)
                }
                authRepository.login(email, password)
                _state.value = LoadState.Idle
            } catch (e: Exception) {
                _state.value = LoadState.Error(debugMessage(e))
            }
        }
    }
}
