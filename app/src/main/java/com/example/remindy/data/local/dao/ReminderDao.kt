package com.example.remindy.data.local.dao

import androidx.room.*
import com.example.remindy.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    /** UI 用: 論理削除されていないものだけを返す */
    @Query("SELECT * FROM reminders WHERE deletedAt IS NULL ORDER BY title")
    fun observeAll(): Flow<List<ReminderEntity>>

    /** アラーム再スケジュール・一括削除チェック用: 有効かつアクティブ */
    @Query("SELECT * FROM reminders WHERE enabled = 1 AND deletedAt IS NULL")
    suspend fun enabled(): List<ReminderEntity>

    /** 一回のみ通知の期限切れチェック用: アクティブ全件 */
    @Query("SELECT * FROM reminders WHERE deletedAt IS NULL")
    suspend fun allActive(): List<ReminderEntity>

    /** 同期用: 未同期の変更（論理削除済みを含む） */
    @Query("SELECT * FROM reminders WHERE synced = 0")
    suspend fun unsynced(): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun findById(id: String): ReminderEntity?

    @Upsert
    suspend fun upsert(entity: ReminderEntity)

    @Query("UPDATE reminders SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM reminders")
    suspend fun clear()
}
