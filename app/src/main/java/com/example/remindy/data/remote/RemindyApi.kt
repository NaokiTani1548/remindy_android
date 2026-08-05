package com.example.remindy.data.remote

import com.example.remindy.data.remote.dto.*
import retrofit2.http.*

interface RemindyApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body body: RegisterRequestDto): RegisterResponseDto

    @POST("api/v1/auth/login")
    suspend fun login(@Body body: LoginRequestDto): LoginResponseDto

    @GET("api/v1/auth/me")
    suspend fun me(): MeResponseDto

    @POST("api/v1/sync")
    suspend fun sync(@Body body: SyncRequestDto): SyncResponseDto
}
