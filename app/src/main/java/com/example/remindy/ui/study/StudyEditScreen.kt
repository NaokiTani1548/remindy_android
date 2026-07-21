package com.example.remindy.ui.study

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        topBar = { TopAppBar(title = { Text(if (existing == null) "新規学習項目" else "学習項目編集") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("種別", style = MaterialTheme.typography.labelLarge)
            Row {
                listOf(StudyItemKind.QA to "質問・回答", StudyItemKind.TERM to "用語・詳細").forEach { (k, label) ->
                    Row(
                        Modifier.selectable(selected = kind == k, onClick = { kind = k }).padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = kind == k, onClick = { kind = k })
                        Text(label)
                    }
                }
            }
            OutlinedTextField(
                value = prompt, onValueChange = { prompt = it },
                label = { Text(if (kind == StudyItemKind.QA) "質問（表）" else "用語（表）") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = answer, onValueChange = { answer = it },
                label = { Text(if (kind == StudyItemKind.QA) "回答（裏）" else "詳細（裏）") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
            )
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Button(
                onClick = {
                    when {
                        prompt.isBlank() || answer.isBlank() -> error = "表と裏の両方を入力してください"
                        existing == null -> { viewModel.create(kind, prompt.trim(), answer.trim()); onDone() }
                        else -> { viewModel.update(existing.id, kind, prompt.trim(), answer.trim()); onDone() }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("保存") }
            OutlinedButton(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("キャンセル") }
        }
    }
}
