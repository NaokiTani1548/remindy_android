package com.example.remindy.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.remindy.domain.model.Schedule
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

private enum class SchedType { ONE_TIME, DAILY, WEEKLY, MONTHLY }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderEditScreen(
    viewModel: ReminderViewModel,
    reminderId: String?,
    onDone: () -> Unit,
) {
    val existing = remember(reminderId) { reminderId?.let { viewModel.findById(it) } }

    var title by remember { mutableStateOf(existing?.title ?: "") }
    var type by remember { mutableStateOf(existing?.schedule.toType()) }
    var time by remember { mutableStateOf(existing?.schedule?.time?.toString() ?: "09:00") }
    var date by remember { mutableStateOf((existing?.schedule as? Schedule.OneTime)?.date?.toString() ?: LocalDate.now().toString()) }
    var dayOfWeek by remember { mutableStateOf((existing?.schedule as? Schedule.Weekly)?.dayOfWeek ?: DayOfWeek.MONDAY) }
    var dayOfMonth by remember { mutableStateOf(((existing?.schedule as? Schedule.Monthly)?.dayOfMonth ?: 1).toString()) }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existing == null) "新規リマインダー" else "リマインダー編集") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                label = { Text("タイトル") }, singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("input_title"),
            )

            Text("繰り返し", style = MaterialTheme.typography.labelLarge)
            Column {
                SchedType.entries.forEach { t ->
                    Row(
                        Modifier.fillMaxWidth().selectable(selected = type == t, onClick = { type = t }),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = type == t, onClick = { type = t })
                        Text(typeLabel(t))
                    }
                }
            }

            OutlinedTextField(
                value = time, onValueChange = { time = it },
                label = { Text("時刻 (HH:mm)") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )

            when (type) {
                SchedType.ONE_TIME -> OutlinedTextField(
                    value = date, onValueChange = { date = it },
                    label = { Text("日付 (YYYY-MM-DD)") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                SchedType.WEEKLY -> WeekdaySelector(dayOfWeek) { dayOfWeek = it }
                SchedType.MONTHLY -> OutlinedTextField(
                    value = dayOfMonth, onValueChange = { dayOfMonth = it },
                    label = { Text("日にち (1-31)") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
                SchedType.DAILY -> {}
            }

            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = {
                    val result = buildSchedule(type, time, date, dayOfWeek, dayOfMonth)
                    when {
                        title.isBlank() -> error = "タイトルを入力してください"
                        result == null -> error = "スケジュールの入力が不正です"
                        existing == null -> { viewModel.create(title.trim(), result); onDone() }
                        else -> { viewModel.update(existing.id, title.trim(), result); onDone() }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("保存") }
            OutlinedButton(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("キャンセル") }
        }
    }
}

@Composable
private fun WeekdaySelector(selected: DayOfWeek, onSelect: (DayOfWeek) -> Unit) {
    Column {
        Text("曜日", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            DayOfWeek.entries.forEach { d ->
                FilterChip(
                    selected = selected == d,
                    onClick = { onSelect(d) },
                    label = { Text(weekdayJa(d.name)) },
                )
            }
        }
    }
}

private fun Schedule?.toType(): SchedType = when (this) {
    is Schedule.OneTime -> SchedType.ONE_TIME
    is Schedule.Daily -> SchedType.DAILY
    is Schedule.Weekly -> SchedType.WEEKLY
    is Schedule.Monthly -> SchedType.MONTHLY
    null -> SchedType.DAILY
}

private fun typeLabel(t: SchedType) = when (t) {
    SchedType.ONE_TIME -> "一回限り"
    SchedType.DAILY -> "毎日"
    SchedType.WEEKLY -> "毎週"
    SchedType.MONTHLY -> "毎月"
}

private fun buildSchedule(
    type: SchedType, timeStr: String, dateStr: String, dow: DayOfWeek, domStr: String,
): Schedule? = try {
    val time = LocalTime.parse(timeStr)
    when (type) {
        SchedType.ONE_TIME -> Schedule.OneTime(LocalDate.parse(dateStr), time)
        SchedType.DAILY -> Schedule.Daily(time)
        SchedType.WEEKLY -> Schedule.Weekly(dow, time)
        SchedType.MONTHLY -> {
            val dom = domStr.toInt()
            if (dom in 1..31) Schedule.Monthly(dom, time) else null
        }
    }
} catch (e: Exception) {
    null
}
