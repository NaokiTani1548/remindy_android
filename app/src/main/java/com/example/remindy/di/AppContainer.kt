package com.example.remindy.di

import android.content.Context
import androidx.room.Room
import com.example.remindy.BuildConfig
import com.example.remindy.data.local.RemindyDatabase
import com.example.remindy.data.local.TokenStore
import com.example.remindy.data.remote.AuthInterceptor
import com.example.remindy.data.remote.RemindyApi
import com.example.remindy.data.repository.*
import com.example.remindy.notification.ReminderAlarmScheduler
import com.example.remindy.notification.StudyAlarmScheduler
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * 手動DIコンテナ。Applicationが1つ生成し、全依存の単一の組み立て場所にする。
 */
class AppContainer(context: Context) {

    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"   // Schedule 判別共用体のキーをバックエンドに合わせる
        encodeDefaults = true
    }

    val tokenStore = TokenStore(context)

    private val okHttp = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor(tokenStore))
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttp)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val api: RemindyApi = retrofit.create(RemindyApi::class.java)

    private val db = Room.databaseBuilder(context, RemindyDatabase::class.java, "remindy.db")
        .fallbackToDestructiveMigration(true)
        .build()

    val healthRepository = HealthRepository(BuildConfig.BASE_URL)

    val authRepository = AuthRepository(api, tokenStore)
    val reminderRepository = ReminderRepository(db.reminderDao())
    val studyRepository = StudyRepository(db.studyItemDao(), db.settingDao())
    val syncRepository = SyncRepository(
        api,
        db.reminderDao(),
        db.studyItemDao(),
        db.settingDao(),
        db.syncMetadataDao(),
    )

    val reminderAlarmScheduler = ReminderAlarmScheduler(context)
    val studyAlarmScheduler = StudyAlarmScheduler(context)
}
