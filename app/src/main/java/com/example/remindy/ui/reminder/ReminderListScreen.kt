package com.example.remindy.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.remindy.R
import com.example.remindy.domain.model.Reminder
import com.example.remindy.domain.model.Schedule
import com.example.remindy.ui.common.ScreenHeader
import com.example.remindy.ui.theme.Amber100
import com.example.remindy.ui.theme.Amber900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderListScreen(
    viewModel: ReminderViewModel,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
) {
    val reminders by viewModel.reminders.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            ScreenHeader(
                imageRes = R.drawable.reminder_header,
                screenTitle = "リマインダー",
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAdd,
                icon = { Icon(Icons.Filled.Add, contentDescription = "追加") },
                text = { Text("追加") },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
            )
        },
    ) { padding ->
        if (reminders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        "リマインダーがありません",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "「追加」ボタンで作成できます",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(reminders, key = { it.id }) { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        onClick = { onEdit(reminder.id) },
                        onToggle = { viewModel.setEnabled(reminder.id, it) },
                        onDelete = { viewModel.delete(reminder.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderCard(
    reminder: Reminder,
    onClick: () -> Unit,
    onToggle: (Boolean) -> Unit,
    onDelete: () -> Unit,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 4.dp,
                top = 10.dp,
                bottom = 10.dp,
            ),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = reminder.enabled,
                    onCheckedChange = onToggle,
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "削除",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            ScheduleBadge(
                schedule = reminder.schedule,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
    }
}

/** スケジュール種別ごとに色付きピルバッジを表示する */
@Composable
private fun ScheduleBadge(schedule: Schedule, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (schedule) {
        is Schedule.OneTime  -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "一回  ${schedule.date}  ${schedule.time}",
        )
        is Schedule.Daily    -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "毎日  ${schedule.time}",
        )
        is Schedule.Weekly   -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "毎週${weekdayJa(schedule.dayOfWeek.name)}曜日  ${schedule.time}",
        )
        is Schedule.Monthly  -> Triple(
            Amber100,
            Amber900,
            "毎月${schedule.dayOfMonth}日  ${schedule.time}",
        )
    }

    Surface(
        modifier = modifier,
        color = bgColor,
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
        )
    }
}

fun scheduleLabel(schedule: Schedule): String = when (schedule) {
    is Schedule.OneTime -> "一回  ${schedule.date}  ${schedule.time}"
    is Schedule.Daily -> "毎日  ${schedule.time}"
    is Schedule.Weekly -> "毎週${weekdayJa(schedule.dayOfWeek.name)}曜日  ${schedule.time}"
    is Schedule.Monthly -> "毎月${schedule.dayOfMonth}日  ${schedule.time}"
}

fun weekdayJa(name: String): String = mapOf(
    "MONDAY" to "月", "TUESDAY" to "火", "WEDNESDAY" to "水", "THURSDAY" to "木",
    "FRIDAY" to "金", "SATURDAY" to "土", "SUNDAY" to "日",
)[name] ?: name
