package com.example.remindy.data.local.dao

import androidx.room.*
import com.example.remindy.data.local.entity.SettingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Query("SELECT * FROM notification_setting WHERE id = 0")
    fun observe(): Flow<SettingEntity?>

    @Query("SELECT * FROM notification_setting WHERE id = 0")
    suspend fun get(): SettingEntity?

    @Upsert
    suspend fun upsert(entity: SettingEntity)
}
