package com.example.remindy.data.local.dao

import androidx.room.*
import com.example.remindy.data.local.entity.StudyItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyItemDao {
    @Query("SELECT * FROM study_items ORDER BY prompt")
    fun observeAll(): Flow<List<StudyItemEntity>>

    @Query("SELECT * FROM study_items WHERE enabled = 1")
    suspend fun enabled(): List<StudyItemEntity>

    @Upsert
    suspend fun upsert(entity: StudyItemEntity)

    @Query("DELETE FROM study_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM study_items")
    suspend fun clear()

    @Transaction
    suspend fun replaceAll(items: List<StudyItemEntity>) {
        clear()
        items.forEach { upsert(it) }
    }
}
