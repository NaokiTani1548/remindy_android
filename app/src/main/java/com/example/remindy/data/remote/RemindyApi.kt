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

    // Reminders
    @POST("api/v1/reminders")
    suspend fun createReminder(@Body body: CreateReminderRequestDto): ReminderResponseDto

    @GET("api/v1/reminders")
    suspend fun listReminders(): ReminderListResponseDto

    @PUT("api/v1/reminders/{id}")
    suspend fun updateReminder(@Path("id") id: String, @Body body: UpdateReminderRequestDto): ReminderResponseDto

    @PATCH("api/v1/reminders/{id}")
    suspend fun toggleReminder(@Path("id") id: String, @Body body: ToggleEnabledRequestDto): ReminderResponseDto

    @DELETE("api/v1/reminders/{id}")
    suspend fun deleteReminder(@Path("id") id: String)

    // Study items
    @POST("api/v1/study-items")
    suspend fun createStudyItem(@Body body: CreateStudyItemRequestDto): StudyItemResponseDto

    @GET("api/v1/study-items")
    suspend fun listStudyItems(): StudyItemListResponseDto

    @PUT("api/v1/study-items/{id}")
    suspend fun updateStudyItem(@Path("id") id: String, @Body body: UpdateStudyItemRequestDto): StudyItemResponseDto

    @PATCH("api/v1/study-items/{id}")
    suspend fun toggleStudyItem(@Path("id") id: String, @Body body: ToggleStudyItemRequestDto): StudyItemResponseDto

    @DELETE("api/v1/study-items/{id}")
    suspend fun deleteStudyItem(@Path("id") id: String)

    // Notification setting
    @GET("api/v1/study/notification-setting")
    suspend fun getNotificationSetting(): NotificationSettingResponseDto

    @PUT("api/v1/study/notification-setting")
    suspend fun updateNotificationSetting(@Body body: UpdateNotificationSettingRequestDto): NotificationSettingResponseDto

    // Sync
    @GET("api/v1/sync")
    suspend fun sync(): SyncResponseDto
}
