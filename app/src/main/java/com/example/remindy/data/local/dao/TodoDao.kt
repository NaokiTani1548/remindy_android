package com.example.remindy.data.local.dao

import androidx.room.*
import com.example.remindy.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE deletedAt IS NULL ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE deletedAt IS NULL ORDER BY createdAt DESC")
    suspend fun allActive(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun findById(id: String): TodoEntity?

    @Upsert
    suspend fun upsert(entity: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteById(id: String)
}
