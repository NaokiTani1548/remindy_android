package com.example.remindy.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.remindy.R
import com.example.remindy.data.local.entity.TodoEntity
import com.example.remindy.di.AppContainer

class TodoWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        TodoRemoteViewsFactory(applicationContext)
}

private class TodoRemoteViewsFactory(
    private val context: Context,
) : RemoteViewsService.RemoteViewsFactory {

    private var items: List<TodoEntity> = emptyList()

    private val dao by lazy {
        (context.applicationContext as com.example.remindy.RemindyApplication)
            .container.db.todoDao()
    }

    override fun onCreate() {}

    override fun onDataSetChanged() {
        items = kotlinx.coroutines.runBlocking {
            dao.allActive()
        }
    }

    override fun onDestroy() {
        items = emptyList()
    }

    override fun getCount(): Int = items.size

    override fun getViewAt(position: Int): RemoteViews {
        val item = items[position]
        return RemoteViews(context.packageName, R.layout.widget_todo_item).apply {
            setTextViewText(R.id.item_title, item.title)
            setOnClickFillInIntent(R.id.item_title, Intent())
        }
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = items[position].id.hashCode().toLong()
    override fun hasStableIds(): Boolean = true
}
