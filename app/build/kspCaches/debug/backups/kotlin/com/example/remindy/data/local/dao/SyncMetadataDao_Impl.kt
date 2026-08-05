package com.example.remindy.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.remindy.`data`.local.entity.SyncMetadataEntity
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SyncMetadataDao_Impl(
  __db: RoomDatabase,
) : SyncMetadataDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfSyncMetadataEntity: EntityUpsertAdapter<SyncMetadataEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfSyncMetadataEntity = EntityUpsertAdapter<SyncMetadataEntity>(object :
        EntityInsertAdapter<SyncMetadataEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `sync_metadata` (`id`,`lastSyncedAt`) VALUES (?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SyncMetadataEntity) {
        statement.bindLong(1, entity.id.toLong())
        val _tmpLastSyncedAt: String? = entity.lastSyncedAt
        if (_tmpLastSyncedAt == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpLastSyncedAt)
        }
      }
    }, object : EntityDeleteOrUpdateAdapter<SyncMetadataEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `sync_metadata` SET `id` = ?,`lastSyncedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SyncMetadataEntity) {
        statement.bindLong(1, entity.id.toLong())
        val _tmpLastSyncedAt: String? = entity.lastSyncedAt
        if (_tmpLastSyncedAt == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpLastSyncedAt)
        }
        statement.bindLong(3, entity.id.toLong())
      }
    })
  }

  public override suspend fun upsert(entity: SyncMetadataEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __upsertAdapterOfSyncMetadataEntity.upsert(_connection, entity)
  }

  public override suspend fun `get`(): SyncMetadataEntity? {
    val _sql: String = "SELECT * FROM sync_metadata WHERE id = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfLastSyncedAt: Int = getColumnIndexOrThrow(_stmt, "lastSyncedAt")
        val _result: SyncMetadataEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpLastSyncedAt: String?
          if (_stmt.isNull(_columnIndexOfLastSyncedAt)) {
            _tmpLastSyncedAt = null
          } else {
            _tmpLastSyncedAt = _stmt.getText(_columnIndexOfLastSyncedAt)
          }
          _result = SyncMetadataEntity(_tmpId,_tmpLastSyncedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
