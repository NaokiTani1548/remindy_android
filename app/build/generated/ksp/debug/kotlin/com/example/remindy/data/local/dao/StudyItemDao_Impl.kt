package com.example.remindy.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.remindy.`data`.local.entity.StudyItemEntity
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
public class StudyItemDao_Impl(
  __db: RoomDatabase,
) : StudyItemDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfStudyItemEntity: EntityUpsertAdapter<StudyItemEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfStudyItemEntity = EntityUpsertAdapter<StudyItemEntity>(object :
        EntityInsertAdapter<StudyItemEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `study_items` (`id`,`kind`,`prompt`,`answer`,`enabled`,`createdAt`,`updatedAt`,`deletedAt`,`synced`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: StudyItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.kind)
        statement.bindText(3, entity.prompt)
        statement.bindText(4, entity.answer)
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.createdAt)
        statement.bindText(7, entity.updatedAt)
        val _tmpDeletedAt: String? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpDeletedAt)
        }
        val _tmp_1: Int = if (entity.synced) 1 else 0
        statement.bindLong(9, _tmp_1.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<StudyItemEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `study_items` SET `id` = ?,`kind` = ?,`prompt` = ?,`answer` = ?,`enabled` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ?,`synced` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: StudyItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.kind)
        statement.bindText(3, entity.prompt)
        statement.bindText(4, entity.answer)
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.createdAt)
        statement.bindText(7, entity.updatedAt)
        val _tmpDeletedAt: String? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpDeletedAt)
        }
        val _tmp_1: Int = if (entity.synced) 1 else 0
        statement.bindLong(9, _tmp_1.toLong())
        statement.bindText(10, entity.id)
      }
    })
  }

  public override suspend fun upsert(entity: StudyItemEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __upsertAdapterOfStudyItemEntity.upsert(_connection, entity)
  }

  public override fun observeAll(): Flow<List<StudyItemEntity>> {
    val _sql: String = "SELECT * FROM study_items WHERE deletedAt IS NULL ORDER BY prompt"
    return createFlow(__db, false, arrayOf("study_items")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _columnIndexOfPrompt: Int = getColumnIndexOrThrow(_stmt, "prompt")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: MutableList<StudyItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StudyItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpKind: String
          _tmpKind = _stmt.getText(_columnIndexOfKind)
          val _tmpPrompt: String
          _tmpPrompt = _stmt.getText(_columnIndexOfPrompt)
          val _tmpAnswer: String
          _tmpAnswer = _stmt.getText(_columnIndexOfAnswer)
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
              StudyItemEntity(_tmpId,_tmpKind,_tmpPrompt,_tmpAnswer,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun enabled(): List<StudyItemEntity> {
    val _sql: String = "SELECT * FROM study_items WHERE enabled = 1 AND deletedAt IS NULL"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _columnIndexOfPrompt: Int = getColumnIndexOrThrow(_stmt, "prompt")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: MutableList<StudyItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StudyItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpKind: String
          _tmpKind = _stmt.getText(_columnIndexOfKind)
          val _tmpPrompt: String
          _tmpPrompt = _stmt.getText(_columnIndexOfPrompt)
          val _tmpAnswer: String
          _tmpAnswer = _stmt.getText(_columnIndexOfAnswer)
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
              StudyItemEntity(_tmpId,_tmpKind,_tmpPrompt,_tmpAnswer,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findById(id: String): StudyItemEntity? {
    val _sql: String = "SELECT * FROM study_items WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _columnIndexOfPrompt: Int = getColumnIndexOrThrow(_stmt, "prompt")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: StudyItemEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpKind: String
          _tmpKind = _stmt.getText(_columnIndexOfKind)
          val _tmpPrompt: String
          _tmpPrompt = _stmt.getText(_columnIndexOfPrompt)
          val _tmpAnswer: String
          _tmpAnswer = _stmt.getText(_columnIndexOfAnswer)
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
              StudyItemEntity(_tmpId,_tmpKind,_tmpPrompt,_tmpAnswer,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun unsynced(): List<StudyItemEntity> {
    val _sql: String = "SELECT * FROM study_items WHERE synced = 0"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _columnIndexOfPrompt: Int = getColumnIndexOrThrow(_stmt, "prompt")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _columnIndexOfSynced: Int = getColumnIndexOrThrow(_stmt, "synced")
        val _result: MutableList<StudyItemEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: StudyItemEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpKind: String
          _tmpKind = _stmt.getText(_columnIndexOfKind)
          val _tmpPrompt: String
          _tmpPrompt = _stmt.getText(_columnIndexOfPrompt)
          val _tmpAnswer: String
          _tmpAnswer = _stmt.getText(_columnIndexOfAnswer)
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
              StudyItemEntity(_tmpId,_tmpKind,_tmpPrompt,_tmpAnswer,_tmpEnabled,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt,_tmpSynced)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markSynced(id: String) {
    val _sql: String = "UPDATE study_items SET synced = 1 WHERE id = ?"
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
    val _sql: String = "DELETE FROM study_items WHERE id = ?"
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
    val _sql: String = "DELETE FROM study_items"
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
