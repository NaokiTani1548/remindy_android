package com.example.remindy.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.remindy.RemindyApplication

class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as RemindyApplication

        // 未ログイン時は同期をスキップ（リトライしない）
        val token = app.container.tokenStore.currentToken()
        if (token.isNullOrBlank()) return Result.success()

        return try {
            app.container.syncRepository.sync()
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
