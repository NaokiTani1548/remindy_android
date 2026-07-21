package com.example.remindy.data.repository

import com.example.remindy.data.local.TokenStore
import com.example.remindy.data.remote.RemindyApi
import com.example.remindy.data.remote.dto.LoginRequestDto
import com.example.remindy.data.remote.dto.RegisterRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(
    private val api: RemindyApi,
    private val tokenStore: TokenStore,
) {
    val isLoggedIn: Flow<Boolean> = tokenStore.tokenFlow.map { it != null }

    suspend fun register(email: String, password: String) {
        api.register(RegisterRequestDto(email, password))
    }

    suspend fun login(email: String, password: String) {
        val res = api.login(LoginRequestDto(email, password))
        tokenStore.save(res.accessToken)
    }

    suspend fun logout() = tokenStore.clear()
}
