package com.example.remindy.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.remindy.`data`.local.entity.ReminderEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ReminderDao_Impl(
  __db: RoomDatabase,
) : ReminderDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfReminderEntity: EntityUpsertAdapter<ReminderEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfReminderEntity = EntityUpsertAdapter<ReminderEntity>(object :
        EntityInsertAdapter<ReminderEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `reminders` (`id`,`title`,`scheduleType`,`scheduleTime`,`scheduleDate`,`scheduleDayOfWeek`,`scheduleDayOfMonth`,`enabled`,`createdAt`,`updatedAt`,`deletedAt`,`synced`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ReminderEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.scheduleType)
        statement.bindText(4, entity.scheduleTime)
        val _tmpScheduleDate: String? = entity.scheduleDate
        if (_tmpScheduleDate == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpScheduleDate)
        }
        val _tmpScheduleDayOfWeek: String? = entity.scheduleDayOfWeek
        if (_tmpScheduleDayOfWeek == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpScheduleDayOfWeek)
        }
        val _tmpScheduleDayOfMonth: Int? = entity.scheduleDayOfMonth
        if (_tmpScheduleDayOfMonth == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmpScheduleDayOfMonth.toLong())
        }
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindText(9, entity.createdAt)
        statement.bindText(10, entity.updatedAt)
        val _tmpDeletedAt: String? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDeletedAt)
        }
        val _tmp_1: Int = if (entity.synced) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<ReminderEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `reminders` SET `id` = ?,`title` = ?,`scheduleType` = ?,`scheduleTime` = ?,`scheduleDate` = ?,`scheduleDayOfWeek` = ?,`scheduleDayOfMonth` = ?,`enabled` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ?,`synced` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: ReminderEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.scheduleType)
        statement.bindText(4, entity.scheduleTime)
        val _tmpScheduleDate: String? = entity.scheduleDate
        if (_tmpScheduleDate == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpScheduleDate)
        }
        val _tmpScheduleDayOfWeek: String? = entity.scheduleDayOfWeek
        if (_tmpScheduleDayOfWeek == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpScheduleDayOfWeek)
        }
        val _tmpScheduleDayOfMonth: Int? = entity.scheduleDayOfMonth
        if (_tmpScheduleDayOfMonth == null) {
          statement.bindNull(7)
        } else {
          statement.bindLong(7, _tmpScheduleDayOfMonth.toLong())
        }
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindText(9, entity.createdAt)
        statement.bindText(10, entity.updatedAt)
        val _tmpDeletedAt: String? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(11)
        } else {
          statement.bindText(11, _tmpDeletedAt)
        }
        val _tmp_1: Int = if (entity.synced) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
        statement.bindText(13, entity.id)
      }
    })
  }

  public override suspend fun upsert(entity: ReminderEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __upsertAdapterOfReminderEntity.upsert(_connection, entity)
  }

  public override fun observeAll(): Flow<List<ReminderEntity>> {
    val _sql: String = "SELECT * FROM reminders WHERE deletedAt IS NULL ORDER BY title"
    return createFlow(__db, false, arrayOf("reminders")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfScheduleType: Int = getColumnIndexOrThrow(_stmt, "scheduleType")
        val _columnIndexOfScheduleTime: Int = getColumnIndexOrThrow(_stmt, "scheduleTime")
        val _columnIndexOfScheduleDate: Int = getColumnIndexOrThrow(_stmt, "scheduleDate")
        val _columnIndexOfScheduleDayOfWeek: Int = getColumnIndexOrThrow(_stmt, "scheduleDayOfWeek")
        val _columnIndexOfScheduleDayOfMonth: Int = getColumnIndexOrThrow(_stmt,
            "scheduleDayOfMonth")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: MutableList<ReminderEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReminderEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpScheduleType: String
          _tmpScheduleType = _stmt.getText(_columnIndexOfScheduleType)
          val _tmpScheduleTime: String
          _tmpScheduleTime = _stmt.getText(_columnIndexOfScheduleTime)
          val _tmpScheduleDate: String?
          if (_stmt.isNull(_columnIndexOfScheduleDate)) {
            _tmpScheduleDate = null
          } else {
            _tmpScheduleDate = _stmt.getText(_columnIndexOfScheduleDate)
          }
          val _tmpScheduleDayOfWeek: String?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfWeek)) {
            _tmpScheduleDayOfWeek = null
          } else {
            _tmpScheduleDayOfWeek = _stmt.getText(_columnIndexOfScheduleDayOfWeek)
          }
          val _tmpScheduleDayOfMonth: Int?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfMonth)) {
            _tmpScheduleDayOfMonth = null
          } else {
            _tmpScheduleDayOfMonth = _stmt.getLong(_columnIndexOfScheduleDayOfMonth).toInt()
          }
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: String?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getText(_columnIndexOfDeletedAt)
          }
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _item =
              ReminderEntity(_tmpId,_tmpTitle,_tmpScheduleType,_tmpScheduleTime,_tmpScheduleDate,_tmpScheduleDayOfWeek,_tmpScheduleDayOfMonth,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun enabled(): List<ReminderEntity> {
    val _sql: String = "SELECT * FROM reminders WHERE enabled = 1 AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfScheduleType: Int = getColumnIndexOrThrow(_stmt, "scheduleType")
        val _columnIndexOfScheduleTime: Int = getColumnIndexOrThrow(_stmt, "scheduleTime")
        val _columnIndexOfScheduleDate: Int = getColumnIndexOrThrow(_stmt, "scheduleDate")
        val _columnIndexOfScheduleDayOfWeek: Int = getColumnIndexOrThrow(_stmt, "scheduleDayOfWeek")
        val _columnIndexOfScheduleDayOfMonth: Int = getColumnIndexOrThrow(_stmt,
            "scheduleDayOfMonth")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: MutableList<ReminderEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReminderEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpScheduleType: String
          _tmpScheduleType = _stmt.getText(_columnIndexOfScheduleType)
          val _tmpScheduleTime: String
          _tmpScheduleTime = _stmt.getText(_columnIndexOfScheduleTime)
          val _tmpScheduleDate: String?
          if (_stmt.isNull(_columnIndexOfScheduleDate)) {
            _tmpScheduleDate = null
          } else {
            _tmpScheduleDate = _stmt.getText(_columnIndexOfScheduleDate)
          }
          val _tmpScheduleDayOfWeek: String?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfWeek)) {
            _tmpScheduleDayOfWeek = null
          } else {
            _tmpScheduleDayOfWeek = _stmt.getText(_columnIndexOfScheduleDayOfWeek)
          }
          val _tmpScheduleDayOfMonth: Int?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfMonth)) {
            _tmpScheduleDayOfMonth = null
          } else {
            _tmpScheduleDayOfMonth = _stmt.getLong(_columnIndexOfScheduleDayOfMonth).toInt()
          }
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: String?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getText(_columnIndexOfDeletedAt)
          }
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _item =
              ReminderEntity(_tmpId,_tmpTitle,_tmpScheduleType,_tmpScheduleTime,_tmpScheduleDate,_tmpScheduleDayOfWeek,_tmpScheduleDayOfMonth,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun allActive(): List<ReminderEntity> {
    val _sql: String = "SELECT * FROM reminders WHERE deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfScheduleType: Int = getColumnIndexOrThrow(_stmt, "scheduleType")
        val _columnIndexOfScheduleTime: Int = getColumnIndexOrThrow(_stmt, "scheduleTime")
        val _columnIndexOfScheduleDate: Int = getColumnIndexOrThrow(_stmt, "scheduleDate")
        val _columnIndexOfScheduleDayOfWeek: Int = getColumnIndexOrThrow(_stmt, "scheduleDayOfWeek")
        val _columnIndexOfScheduleDayOfMonth: Int = getColumnIndexOrThrow(_stmt,
            "scheduleDayOfMonth")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: MutableList<ReminderEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReminderEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpScheduleType: String
          _tmpScheduleType = _stmt.getText(_columnIndexOfScheduleType)
          val _tmpScheduleTime: String
          _tmpScheduleTime = _stmt.getText(_columnIndexOfScheduleTime)
          val _tmpScheduleDate: String?
          if (_stmt.isNull(_columnIndexOfScheduleDate)) {
            _tmpScheduleDate = null
          } else {
            _tmpScheduleDate = _stmt.getText(_columnIndexOfScheduleDate)
          }
          val _tmpScheduleDayOfWeek: String?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfWeek)) {
            _tmpScheduleDayOfWeek = null
          } else {
            _tmpScheduleDayOfWeek = _stmt.getText(_columnIndexOfScheduleDayOfWeek)
          }
          val _tmpScheduleDayOfMonth: Int?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfMonth)) {
            _tmpScheduleDayOfMonth = null
          } else {
            _tmpScheduleDayOfMonth = _stmt.getLong(_columnIndexOfScheduleDayOfMonth).toInt()
          }
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: String?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getText(_columnIndexOfDeletedAt)
          }
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _item =
              ReminderEntity(_tmpId,_tmpTitle,_tmpScheduleType,_tmpScheduleTime,_tmpScheduleDate,_tmpScheduleDayOfWeek,_tmpScheduleDayOfMonth,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun unsynced(): List<ReminderEntity> {
    val _sql: String = "SELECT * FROM reminders WHERE synced = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfScheduleType: Int = getColumnIndexOrThrow(_stmt, "scheduleType")
        val _columnIndexOfScheduleTime: Int = getColumnIndexOrThrow(_stmt, "scheduleTime")
        val _columnIndexOfScheduleDate: Int = getColumnIndexOrThrow(_stmt, "scheduleDate")
        val _columnIndexOfScheduleDayOfWeek: Int = getColumnIndexOrThrow(_stmt, "scheduleDayOfWeek")
        val _columnIndexOfScheduleDayOfMonth: Int = getColumnIndexOrThrow(_stmt,
            "scheduleDayOfMonth")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: MutableList<ReminderEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReminderEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpScheduleType: String
          _tmpScheduleType = _stmt.getText(_columnIndexOfScheduleType)
          val _tmpScheduleTime: String
          _tmpScheduleTime = _stmt.getText(_columnIndexOfScheduleTime)
          val _tmpScheduleDate: String?
          if (_stmt.isNull(_columnIndexOfScheduleDate)) {
            _tmpScheduleDate = null
          } else {
            _tmpScheduleDate = _stmt.getText(_columnIndexOfScheduleDate)
          }
          val _tmpScheduleDayOfWeek: String?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfWeek)) {
            _tmpScheduleDayOfWeek = null
          } else {
            _tmpScheduleDayOfWeek = _stmt.getText(_columnIndexOfScheduleDayOfWeek)
          }
          val _tmpScheduleDayOfMonth: Int?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfMonth)) {
            _tmpScheduleDayOfMonth = null
          } else {
            _tmpScheduleDayOfMonth = _stmt.getLong(_columnIndexOfScheduleDayOfMonth).toInt()
          }
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: String?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getText(_columnIndexOfDeletedAt)
          }
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _item =
              ReminderEntity(_tmpId,_tmpTitle,_tmpScheduleType,_tmpScheduleTime,_tmpScheduleDate,_tmpScheduleDayOfWeek,_tmpScheduleDayOfMonth,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findById(id: String): ReminderEntity? {
    val _sql: String = "SELECT * FROM reminders WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfScheduleType: Int = getColumnIndexOrThrow(_stmt, "scheduleType")
        val _columnIndexOfScheduleTime: Int = getColumnIndexOrThrow(_stmt, "scheduleTime")
        val _columnIndexOfScheduleDate: Int = getColumnIndexOrThrow(_stmt, "scheduleDate")
        val _columnIndexOfScheduleDayOfWeek: Int = getColumnIndexOrThrow(_stmt, "scheduleDayOfWeek")
        val _columnIndexOfScheduleDayOfMonth: Int = getColumnIndexOrThrow(_stmt,
            "scheduleDayOfMonth")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: ReminderEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpScheduleType: String
          _tmpScheduleType = _stmt.getText(_columnIndexOfScheduleType)
          val _tmpScheduleTime: String
          _tmpScheduleTime = _stmt.getText(_columnIndexOfScheduleTime)
          val _tmpScheduleDate: String?
          if (_stmt.isNull(_columnIndexOfScheduleDate)) {
            _tmpScheduleDate = null
          } else {
            _tmpScheduleDate = _stmt.getText(_columnIndexOfScheduleDate)
          }
          val _tmpScheduleDayOfWeek: String?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfWeek)) {
            _tmpScheduleDayOfWeek = null
          } else {
            _tmpScheduleDayOfWeek = _stmt.getText(_columnIndexOfScheduleDayOfWeek)
          }
          val _tmpScheduleDayOfMonth: Int?
          if (_stmt.isNull(_columnIndexOfScheduleDayOfMonth)) {
            _tmpScheduleDayOfMonth = null
          } else {
            _tmpScheduleDayOfMonth = _stmt.getLong(_columnIndexOfScheduleDayOfMonth).toInt()
          }
          val _tmpEnabled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfEnabled).toInt()
          _tmpEnabled = _tmp != 0
          val _tmpCreatedAt: String
          _tmpCreatedAt = _stmt.getText(_columnIndexOfCreatedAt)
          val _tmpUpdatedAt: String
          _tmpUpdatedAt = _stmt.getText(_columnIndexOfUpdatedAt)
          val _tmpDeletedAt: String?
          if (_stmt.isNull(_columnIndexOfDeletedAt)) {
            _tmpDeletedAt = null
          } else {
            _tmpDeletedAt = _stmt.getText(_columnIndexOfDeletedAt)
          }
          val _tmpSynced: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfSynced).toInt()
          _tmpSynced = _tmp_1 != 0
          _result =
              ReminderEntity(_tmpId,_tmpTitle,_tmpScheduleType,_tmpScheduleTime,_tmpScheduleDate,_tmpScheduleDayOfWeek,_tmpScheduleDayOfMonth,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSynced(id: String) {
    val _sql: String = "UPDATE reminders SET synced = 1 WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: String) {
    val _sql: String = "DELETE FROM reminders WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun clear() {
    val _sql: String = "DELETE FROM reminders"
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
