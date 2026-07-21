package com.example.remindy.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.remindy.domain.model.Frequency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val setting by viewModel.setting.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("学習通知設定") }) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            val current = setting
            if (current == null) {
                CircularProgressIndicator()
                return@Column
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("通知を有効にする", modifier = Modifier.weight(1f))
                Switch(
                    checked = current.enabled,
                    onCheckedChange = { viewModel.update(current.frequency, it) },
                )
            }
            HorizontalDivider()

            Text("1日の通知回数", style = MaterialTheme.typography.labelLarge)
            listOf(
                Frequency.ONCE to "1回",
                Frequency.THREE_TIMES to "3回",
                Frequency.FIVE_TIMES to "5回",
            ).forEach { (freq, label) ->
                Row(
                    Modifier.fillMaxWidth().selectable(
                        selected = current.frequency == freq,
                        onClick = { viewModel.update(freq, current.enabled) },
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = current.frequency == freq,
                        onClick = { viewModel.update(freq, current.enabled) },
                    )
                    Text(label)
                }
            }

            HorizontalDivider()
            Text(
                "通知時間帯は 9:00〜21:00 に固定です（回数に応じて均等配置）。",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
