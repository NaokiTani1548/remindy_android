package com.example.remindy.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.remindy.`data`.local.entity.SettingEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SettingDao_Impl(
  __db: RoomDatabase,
) : SettingDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfSettingEntity: EntityUpsertAdapter<SettingEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfSettingEntity = EntityUpsertAdapter<SettingEntity>(object :
        EntityInsertAdapter<SettingEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `notification_setting` (`id`,`frequency`,`enabled`,`updatedAt`,`synced`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SettingEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.frequency)
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        statement.bindText(4, entity.updatedAt)
        val _tmp_1: Int = if (entity.synced) 1 else 0
        statement.bindLong(5, _tmp_1.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<SettingEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `notification_setting` SET `id` = ?,`frequency` = ?,`enabled` = ?,`updatedAt` = ?,`synced` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SettingEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.frequency)
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        statement.bindText(4, entity.updatedAt)
        val _tmp_1: Int = if (entity.synced) 1 else 0
        statement.bindLong(5, _tmp_1.toLong())
        statement.bindLong(6, entity.id.toLong())
      }
    })
  }

  public override suspend fun upsert(entity: SettingEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __upsertAdapterOfSettingEntity.upsert(_connection, entity)
  }

  public override fun observe(): Flow<SettingEntity?> {
    val _sql: String = "SELECT * FROM notification_setting WHERE id = 0"
    return createFlow(__db, false, arrayOf("notification_setting")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: SettingEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _result = SettingEntity(_tmpId,_tmpFrequency,_tmpEnabled,_tmpUpdatedAt,_tmpSynced)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun `get`(): SettingEntity? {
    val _sql: String = "SELECT * FROM notification_setting WHERE id = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: SettingEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _result = SettingEntity(_tmpId,_tmpFrequency,_tmpEnabled,_tmpUpdatedAt,_tmpSynced)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUnsynced(): SettingEntity? {
    val _sql: String = "SELECT * FROM notification_setting WHERE id = 0 AND synced = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfFrequency: Int = getColumnIndexOrThrow(_stmt, "frequency")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: SettingEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpFrequency: String
          _tmpFrequency = _stmt.getText(_columnIndexOfFrequency)
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _result = SettingEntity(_tmpId,_tmpFrequency,_tmpEnabled,_tmpUpdatedAt,_tmpSynced)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSynced() {
    val _sql: String = "UPDATE notification_setting SET synced = 1 WHERE id = 0"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
