package com.example.remindy.ui.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.remindy.domain.model.StudyItemKind

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyEditScreen(
    viewModel: StudyViewModel,
    studyItemId: String?,
    onDone: () -> Unit,
) {
    val existing = remember(studyItemId) { studyItemId?.let { viewModel.findById(it) } }
    var kind by remember { mutableStateOf(existing?.kind ?: StudyItemKind.QA) }
    var prompt by remember { mutableStateOf(existing?.prompt ?: "") }
    var answer by remember { mutableStateOf(existing?.answer ?: "") }
    var error by remember { mutableStateOf<String?>(null) }

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
                            text = if (existing == null) "新規学習項目" else "学習項目編集",
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
            // ── 種別 ────────────────────────────────────
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        "種別",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        listOf(StudyItemKind.QA to "質問・回答", StudyItemKind.TERM to "用語・詳細")
                            .forEachIndexed { index, (k, label) ->
                                SegmentedButton(
                                    selected = kind == k,
                                    onClick = { kind = k },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                                ) {
                                    Text(label, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                    }
                }
            }

            // ── 内容 ────────────────────────────────────
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        "内容",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    OutlinedTextField(
                        value = prompt,
                        onValueChange = { prompt = it },
                        label = { Text(if (kind == StudyItemKind.QA) "質問（表）" else "用語（表）") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text(if (kind == StudyItemKind.QA) "回答（裏）" else "詳細（裏）") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                    )
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
                    when {
                        prompt.isBlank() || answer.isBlank() -> error = "表と裏の両方を入力してください"
                        existing == null -> { viewModel.create(kind, prompt.trim(), answer.trim()); onDone() }
                        else -> { viewModel.update(existing.id, kind, prompt.trim(), answer.trim()); onDone() }
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
