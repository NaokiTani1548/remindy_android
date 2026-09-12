package com.example.remindy.data.repository

import android.content.Context
import com.example.remindy.data.local.dao.TodoDao
import com.example.remindy.data.local.entity.TodoEntity
import com.example.remindy.domain.model.Todo
import com.example.remindy.util.UuidV7
import com.example.remindy.util.nowUtc
import com.example.remindy.widget.TodoWidgetProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepository(private val dao: TodoDao, private val context: Context) {

    fun observeAll(): Flow<List<Todo>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    suspend fun findById(id: String): Todo? = dao.findById(id)?.toDomain()

    suspend fun create(title: String, description: String) {
        val now = nowUtc()
        dao.upsert(TodoEntity(
            id = UuidV7.generate(),
            title = title,
            description = description,
            createdAt = now,
            updatedAt = now,
            deletedAt = null,
        ))
        TodoWidgetProvider.sendRefresh(context)
    }

    suspend fun update(id: String, title: String, description: String) {
        val existing = dao.findById(id) ?: return
        dao.upsert(existing.copy(
            title = title,
            description = description,
            updatedAt = nowUtc(),
        ))
        TodoWidgetProvider.sendRefresh(context)
    }

    suspend fun delete(id: String) {
        val existing = dao.findById(id) ?: return
        val now = nowUtc()
        dao.upsert(existing.copy(deletedAt = now, updatedAt = now))
        TodoWidgetProvider.sendRefresh(context)
    }

    private fun TodoEntity.toDomain() = Todo(id = id, title = title, description = description)
}
