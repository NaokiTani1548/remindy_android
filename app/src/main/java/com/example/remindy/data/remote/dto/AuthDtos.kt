package com.example.remindy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(val emailAddress: String, val password: String)

@Serializable
data class RegisterResponseDto(val id: String, val emailAddress: String)

@Serializable
data class LoginRequestDto(val emailAddress: String, val password: String)

@Serializable
data class LoginResponseDto(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
)

@Serializable
data class MeResponseDto(val id: String, val emailAddress: String)
