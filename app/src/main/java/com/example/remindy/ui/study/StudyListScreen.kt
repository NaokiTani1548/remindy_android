package com.example.remindy.ui.study

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
import com.example.remindy.domain.model.StudyItemKind

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyListScreen(
    viewModel: StudyViewModel,
    onAdd: () -> Unit,
    onEdit: (String) -> Unit,
) {
    val items by viewModel.items.collectAsState()
    Scaffold(
        topBar = { TopAppBar(title = { Text("学習項目") }) },
        floatingActionButton = { FloatingActionButton(onClick = onAdd) { Icon(Icons.Filled.Add, "追加") } },
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(items, key = { it.id }) { item ->
                ListItem(
                    overlineContent = { Text(if (item.kind == StudyItemKind.QA) "質問・回答" else "用語・詳細") },
                    headlineContent = { Text(item.prompt) },
                    supportingContent = { Text(item.answer) },
                    trailingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(checked = item.enabled, onCheckedChange = { viewModel.setEnabled(item.id, it) })
                            IconButton(onClick = { viewModel.delete(item.id) }) { Icon(Icons.Filled.Delete, "削除") }
                        }
                    },
                    modifier = Modifier.clickable { onEdit(item.id) },
                )
                HorizontalDivider()
            }
        }
    }
}
