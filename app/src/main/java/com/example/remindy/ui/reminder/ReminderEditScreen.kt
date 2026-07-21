package com.example.remindy.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
                title = {
                    Text(
                        if (existing == null) "新規リマインダー" else "リマインダー編集",
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── タイトル ───────────────────────────────
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        "タイトル",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("リマインダー名を入力") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_title"),
                    )
                }
            }

            // ── スケジュール ────────────────────────────
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        "繰り返し",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        SchedType.entries.forEachIndexed { index, t ->
                            SegmentedButton(
                                selected = type == t,
                                onClick = { type = t },
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = SchedType.entries.size,
                                ),
                            ) {
                                Text(typeLabel(t), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("時刻 (HH:mm)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    when (type) {
                        SchedType.ONE_TIME -> OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("日付 (YYYY-MM-DD)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        SchedType.WEEKLY -> WeekdaySelector(dayOfWeek) { dayOfWeek = it }
                        SchedType.MONTHLY -> OutlinedTextField(
                            value = dayOfMonth,
                            onValueChange = { dayOfMonth = it },
                            label = { Text("日にち (1〜31)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        SchedType.DAILY -> {}
                    }
                }
            }

            // ── エラー表示 ──────────────────────────────
            error?.let {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            // ── アクションボタン ────────────────────────
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text("保存", style = MaterialTheme.typography.labelLarge)
            }

            OutlinedButton(
                onClick = onDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
            ) {
                Text("キャンセル", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeekdaySelector(selected: DayOfWeek, onSelect: (DayOfWeek) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "曜日",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            DayOfWeek.entries.forEachIndexed { index, d ->
                SegmentedButton(
                    selected = selected == d,
                    onClick = { onSelect(d) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = DayOfWeek.entries.size,
                    ),
                ) {
                    Text(weekdayJa(d.name))
                }
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
    SchedType.ONE_TIME -> "一回"
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
