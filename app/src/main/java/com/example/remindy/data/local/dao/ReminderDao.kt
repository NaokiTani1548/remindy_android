package com.example.remindy.data.local.dao

import androidx.room.*
import com.example.remindy.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY title")
    fun observeAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE enabled = 1")
    suspend fun enabled(): List<ReminderEntity>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun findById(id: String): ReminderEntity?

    @Upsert
    suspend fun upsert(entity: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM reminders")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(items: List<ReminderEntity>) {
        clear()
        items.forEach { upsert(it) }
    }
}
