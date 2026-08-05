package com.example.remindy.ui.reminder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.remindy.domain.model.Schedule
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
    var selectedTime by remember {
        mutableStateOf(existing?.schedule?.time ?: LocalTime.of(9, 0))
    }
    var selectedDate by remember {
        mutableStateOf((existing?.schedule as? Schedule.OneTime)?.date ?: LocalDate.now())
    }
    var dayOfWeek by remember {
        mutableStateOf((existing?.schedule as? Schedule.Weekly)?.dayOfWeek ?: DayOfWeek.MONDAY)
    }
    var dayOfMonth by remember {
        mutableStateOf(((existing?.schedule as? Schedule.Monthly)?.dayOfMonth ?: 1).toString())
    }
    var error by remember { mutableStateOf<String?>(null) }

    var showTimePicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // ── 時刻ピッカーダイアログ ───────────────────────
    if (showTimePicker) {
        val state = rememberTimePickerState(
            initialHour = selectedTime.hour,
            initialMinute = selectedTime.minute,
            is24Hour = true,
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("時刻を選択") },
            text = {
                TimePicker(state = state)
            },
            confirmButton = {
                TextButton(onClick = {
                    selectedTime = LocalTime.of(state.hour, state.minute)
                    showTimePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("キャンセル") }
            },
        )
    }

    // ── 日付ピッカーダイアログ ───────────────────────
    if (showDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli(),
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millis ->
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("キャンセル") }
            },
        ) {
            DatePicker(state = state)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                        Text(
                            text = "Remindy",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        )
                        Text(
                            text = if (existing == null) "新規リマインダー" else "リマインダー編集",
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
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

                    // 時刻：時計UIで選択
                    Box {
                        OutlinedTextField(
                            value = selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("時刻") },
                            trailingIcon = {
                                Icon(Icons.Default.Schedule, contentDescription = "時刻を選択")
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        // TextField全体をタップ可能にするための透明オーバーレイ
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showTimePicker = true },
                        )
                    }

                    when (type) {
                        SchedType.ONE_TIME -> {
                            // 日付：カレンダーUIで選択
                            Box {
                                OutlinedTextField(
                                    value = selectedDate.format(
                                        DateTimeFormatter.ofPattern("yyyy年M月d日")
                                    ),
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("日付") },
                                    trailingIcon = {
                                        Icon(Icons.Default.CalendarToday, contentDescription = "日付を選択")
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clickable { showDatePicker = true },
                                )
                            }
                        }
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
                    val result = buildSchedule(type, selectedTime, selectedDate, dayOfWeek, dayOfMonth)
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
                    Text(weekdayJa(d))
                }
            }
        }
    }
}

private fun weekdayJa(d: DayOfWeek) = when (d) {
    DayOfWeek.MONDAY -> "月"
    DayOfWeek.TUESDAY -> "火"
    DayOfWeek.WEDNESDAY -> "水"
    DayOfWeek.THURSDAY -> "木"
    DayOfWeek.FRIDAY -> "金"
    DayOfWeek.SATURDAY -> "土"
    DayOfWeek.SUNDAY -> "日"
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
    type: SchedType,
    time: LocalTime,
    date: LocalDate,
    dow: DayOfWeek,
    domStr: String,
): Schedule? = try {
    when (type) {
        SchedType.ONE_TIME -> Schedule.OneTime(date, time)
        SchedType.DAILY -> Schedule.Daily(time)
        SchedType.WEEKLY -> Schedule.Weekly(dow, time)
        SchedType.MONTHLY -> {
            val dom = domStr.toInt()
            if (dom in 1..31) Schedule.Monthly(dom, time) else null
        }
    }
} catch (_: Exception) {
    null
}
