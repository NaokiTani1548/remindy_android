package com.example.remindy.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class HealthRepository(baseUrl: String) {

    // BASE_URL が末尾スラッシュあり・なし両対応
    private val base = if (baseUrl.endsWith("/")) baseUrl else "$baseUrl/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    /** /health を叩き、200 なら true を返す（即時）。 */
    suspend fun checkHealth(): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val request = Request.Builder().url("${base}health").build()
            client.newCall(request).execute().use { it.code == 200 }
        }.getOrDefault(false)
    }
}
