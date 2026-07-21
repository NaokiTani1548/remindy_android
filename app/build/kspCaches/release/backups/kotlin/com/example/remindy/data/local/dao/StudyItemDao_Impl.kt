package com.example.remindy.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performInTransactionSuspending
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
          "INSERT INTO `study_items` (`id`,`kind`,`prompt`,`answer`,`enabled`) VALUES (?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: StudyItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.kind)
        statement.bindText(3, entity.prompt)
        statement.bindText(4, entity.answer)
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(5, _tmp.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<StudyItemEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `study_items` SET `id` = ?,`kind` = ?,`prompt` = ?,`answer` = ?,`enabled` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: StudyItemEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.kind)
        statement.bindText(3, entity.prompt)
        statement.bindText(4, entity.answer)
        val _tmp: Int = if (entity.enabled) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.id)
      }
    })
  }

  public override suspend fun replaceAll(items: List<StudyItemEntity>): Unit =
      performInTransactionSuspending(__db) {
    super@StudyItemDao_Impl.replaceAll(items)
  }

  public override suspend fun upsert(entity: StudyItemEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __upsertAdapterOfStudyItemEntity.upsert(_connection, entity)
  }

  public override fun observeAll(): Flow<List<StudyItemEntity>> {
    val _sql: String = "SELECT * FROM study_items ORDER BY prompt"
    return createFlow(__db, false, arrayOf("study_items")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _columnIndexOfPrompt: Int = getColumnIndexOrThrow(_stmt, "prompt")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
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
          _item = StudyItemEntity(_tmpId,_tmpKind,_tmpPrompt,_tmpAnswer,_tmpEnabled)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun enabled(): List<StudyItemEntity> {
    val _sql: String = "SELECT * FROM study_items WHERE enabled = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfKind: Int = getColumnIndexOrThrow(_stmt, "kind")
        val _columnIndexOfPrompt: Int = getColumnIndexOrThrow(_stmt, "prompt")
        val _columnIndexOfAnswer: Int = getColumnIndexOrThrow(_stmt, "answer")
        val _columnIndexOfEnabled: Int = getColumnIndexOrThrow(_stmt, "enabled")
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
          _item = StudyItemEntity(_tmpId,_tmpKind,_tmpPrompt,_tmpAnswer,_tmpEnabled)
          _result.add(_item)
        }
        _result
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
