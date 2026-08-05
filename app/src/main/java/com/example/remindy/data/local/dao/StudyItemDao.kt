package com.example.remindy.data.local.dao

import androidx.room.*
import com.example.remindy.data.local.entity.StudyItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyItemDao {
    @Query("SELECT * FROM study_items WHERE deletedAt IS NULL ORDER BY prompt")
    fun observeAll(): Flow<List<StudyItemEntity>>

    @Query("SELECT * FROM study_items WHERE enabled = 1 AND deletedAt IS NULL")
    suspend fun enabled(): List<StudyItemEntity>

    @Query("SELECT * FROM study_items WHERE id = :id")
    suspend fun findById(id: String): StudyItemEntity?

    @Query("SELECT * FROM study_items WHERE synced = 0")
    suspend fun unsynced(): List<StudyItemEntity>

    @Upsert
    suspend fun upsert(entity: StudyItemEntity)

    @Query("UPDATE study_items SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: String)

    @Query("DELETE FROM study_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM study_items")
    suspend fun clear()
}
