package com.example.remindy.data.local.dao

import androidx.room.*
import com.example.remindy.data.local.entity.SyncMetadataEntity

@Dao
interface SyncMetadataDao {
    @Query("SELECT * FROM sync_metadata WHERE id = 1")
    suspend fun get(): SyncMetadataEntity?

    @Upsert
    suspend fun upsert(entity: SyncMetadataEntity)
}
