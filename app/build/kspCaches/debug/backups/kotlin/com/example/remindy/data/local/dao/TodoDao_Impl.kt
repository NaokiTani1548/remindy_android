package com.example.remindy.`data`.local.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.remindy.`data`.local.entity.TodoEntity
import javax.`annotation`.processing.Generated
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
public class TodoDao_Impl(
  __db: RoomDatabase,
) : TodoDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfTodoEntity: EntityUpsertAdapter<TodoEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfTodoEntity = EntityUpsertAdapter<TodoEntity>(object :
        EntityInsertAdapter<TodoEntity>() {
      protected override fun createQuery(): String =
          "INSERT INTO `todos` (`id`,`title`,`description`,`createdAt`,`updatedAt`,`deletedAt`) VALUES (?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TodoEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.description)
        statement.bindText(4, entity.createdAt)
        statement.bindText(5, entity.updatedAt)
        val _tmpDeletedAt: String? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpDeletedAt)
        }
      }
    }, object : EntityDeleteOrUpdateAdapter<TodoEntity>() {
      protected override fun createQuery(): String =
          "UPDATE `todos` SET `id` = ?,`title` = ?,`description` = ?,`createdAt` = ?,`updatedAt` = ?,`deletedAt` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TodoEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.description)
        statement.bindText(4, entity.createdAt)
        statement.bindText(5, entity.updatedAt)
        val _tmpDeletedAt: String? = entity.deletedAt
        if (_tmpDeletedAt == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpDeletedAt)
        }
        statement.bindText(7, entity.id)
      }
    })
  }

  public override suspend fun upsert(entity: TodoEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __upsertAdapterOfTodoEntity.upsert(_connection, entity)
  }

  public override fun observeAll(): Flow<List<TodoEntity>> {
    val _sql: String = "SELECT * FROM todos WHERE deletedAt IS NULL ORDER BY createdAt DESC"
    return createFlow(__db, false, arrayOf("todos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<TodoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TodoEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          _item =
              TodoEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun allActive(): List<TodoEntity> {
    val _sql: String = "SELECT * FROM todos WHERE deletedAt IS NULL ORDER BY createdAt DESC"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: MutableList<TodoEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TodoEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          _item =
              TodoEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun findById(id: String): TodoEntity? {
    val _sql: String = "SELECT * FROM todos WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfUpdatedAt: Int = getColumnIndexOrThrow(_stmt, "updatedAt")
        val _columnIndexOfDeletedAt: Int = getColumnIndexOrThrow(_stmt, "deletedAt")
        val _result: TodoEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
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
          _result =
              TodoEntity(_tmpId,_tmpTitle,_tmpDescription,_tmpCreatedAt,_tmpUpdatedAt,_tmpDeletedAt)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: String) {
    val _sql: String = "DELETE FROM todos WHERE id = ?"
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

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
