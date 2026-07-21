package com.example.remindy.ui.reminder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remindy.domain.model.Reminder
import com.example.remindy.domain.model.Schedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderListScreen(
    viewModel: ReminderViewModel,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
) {
    val reminders by viewModel.reminders.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("リマインダー") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) { Icon(Icons.Filled.Add, "追加") }
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(reminders, key = { it.id }) { reminder ->
                ReminderRow(
                    reminder = reminder,
                    onClick = { onEdit(reminder.id) },
                    onToggle = { viewModel.setEnabled(reminder.id, it) },
                    onDelete = { viewModel.delete(reminder.id) },
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun ReminderRow(
    reminder: Reminder,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(reminder.title) },
        supportingContent = { Text(scheduleLabel(reminder.schedule)) },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = reminder.enabled, onCheckedChange = onToggle)
                IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, "削除") }
            }
        },
        modifier = Modifier.clickable(onClick = onClick),
    )
}

fun scheduleLabel(schedule: Schedule): String = when (schedule) {
    is Schedule.OneTime -> "一回 ${schedule.date} ${schedule.time}"
    is Schedule.Daily -> "毎日 ${schedule.time}"
    is Schedule.Weekly -> "毎週 ${weekdayJa(schedule.dayOfWeek.name)} ${schedule.time}"
    is Schedule.Monthly -> "毎月 ${schedule.dayOfMonth}日 ${schedule.time}"
}

fun weekdayJa(name: String): String = mapOf(
    "MONDAY" to "月", "TUESDAY" to "火", "WEDNESDAY" to "水", "THURSDAY" to "木",
    "FRIDAY" to "金", "SATURDAY" to "土", "SUNDAY" to "日",
)[name] ?: name
