package com.example.remindy.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.remindy.data.repository.AuthRepository
import com.example.remindy.ui.auth.AuthViewModel
import com.example.remindy.ui.auth.LoginScreen
import com.example.remindy.ui.connection.ConnectionStatus
import com.example.remindy.ui.connection.ConnectionViewModel
import com.example.remindy.ui.reminder.ReminderEditScreen
import com.example.remindy.ui.reminder.ReminderListScreen
import com.example.remindy.ui.reminder.ReminderViewModel
import com.example.remindy.ui.settings.SettingsScreen
import com.example.remindy.ui.settings.SettingsViewModel
import com.example.remindy.ui.study.StudyEditScreen
import com.example.remindy.ui.study.StudyListScreen
import com.example.remindy.ui.study.StudyViewModel
import com.example.remindy.ui.todo.TodoEditScreen
import com.example.remindy.ui.todo.TodoListScreen
import com.example.remindy.ui.todo.TodoViewModel

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    TabItem("reminders", "リマインダー", Icons.Filled.Notifications),
    TabItem("todos", "ToDo", Icons.Filled.CheckBox),
    TabItem("studies", "学習", Icons.Filled.School),
    TabItem("settings", "設定", Icons.Filled.Settings),
)

@Composable
fun RemindyApp(factory: RemindyViewModelFactory, authRepository: AuthRepository) {
    val loggedIn by authRepository.isLoggedIn.collectAsState(initial = false)
    if (!loggedIn) {
        LoginScreen(viewModel = viewModel(factory = factory))
        return
    }
    MainScaffold(factory)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScaffold(factory: RemindyViewModelFactory) {
    val navController = rememberNavController()

    // 主要ViewModelはActivityスコープで共有（一覧と編集で同一インスタンス→編集時に既存データを参照できる）
    val authVm: AuthViewModel = viewModel(factory = factory)
    val reminderVm: ReminderViewModel = viewModel(factory = factory)
    val studyVm: StudyViewModel = viewModel(factory = factory)
    val todoVm: TodoViewModel = viewModel(factory = factory)
    val settingsVm: SettingsViewModel = viewModel(factory = factory)
    val connectionVm: ConnectionViewModel = viewModel(factory = factory)

    val connectionStatus by connectionVm.status.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                Column {
                    ConnectionStatusBar(
                        status = connectionStatus,
                        onConnect = { connectionVm.connect() },
                    )
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        val backStack by navController.currentBackStackEntryAsState()
                        val current = backStack?.destination
                        tabs.forEach { tab ->
                            NavigationBarItem(
                                selected = current?.hierarchy?.any { it.route == tab.route } == true,
                                onClick = {
                                    navController.navigate(tab.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.label) },
                                label = { Text(tab.label) },
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = "reminders",
                modifier = Modifier.padding(padding),
            ) {
                composable("todos") {
                    TodoListScreen(
                        viewModel = todoVm,
                        onAdd = { navController.navigate("todo_edit") },
                        onEdit = { id -> navController.navigate("todo_edit?id=$id") },
                    )
                }
                composable(
                    route = "todo_edit?id={id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.StringType; nullable = true; defaultValue = null
                    }),
                ) { entry ->
                    TodoEditScreen(
                        viewModel = todoVm,
                        todoId = entry.arguments?.getString("id"),
                        onDone = { navController.popBackStack() },
                    )
                }
                composable("reminders") {
                    ReminderListScreen(
                        viewModel = reminderVm,
                        onAdd = { navController.navigate("reminder_edit") },
                        onEdit = { id -> navController.navigate("reminder_edit?id=$id") },
                    )
                }
                composable(
                    route = "reminder_edit?id={id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.StringType; nullable = true; defaultValue = null
                    }),
                ) { entry ->
                    ReminderEditScreen(
                        viewModel = reminderVm,
                        reminderId = entry.arguments?.getString("id"),
                        onDone = { navController.popBackStack() },
                    )
                }
                composable("studies") {
                    StudyListScreen(
                        viewModel = studyVm,
                        onAdd = { navController.navigate("study_edit") },
                        onEdit = { id -> navController.navigate("study_edit?id=$id") },
                    )
                }
                composable(
                    route = "study_edit?id={id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.StringType; nullable = true; defaultValue = null
                    }),
                ) { entry ->
                    StudyEditScreen(
                        viewModel = studyVm,
                        studyItemId = entry.arguments?.getString("id"),
                        onDone = { navController.popBackStack() },
                    )
                }
                composable("settings") {
                    SettingsScreen(
                        viewModel = settingsVm,
                        onLogout = { authVm.logout() },
                    )
                }
            }
        }

        // 接続中はフルスクリーンオーバーレイでユーザー操作をブロック
        if (connectionStatus is ConnectionStatus.Connecting) {
            ConnectingOverlay(onCancel = { connectionVm.cancelConnect() })
        }
    }
}

/** ナビゲーションバー上の接続ステータス帯。ランプ＋テキスト＋接続ボタン（未接続時のみ）。 */
@Composable
private fun ConnectionStatusBar(
    status: ConnectionStatus,
    onConnect: () -> Unit,
) {
    val dotColor = when (status) {
        ConnectionStatus.Connected -> Color(0xFF4CAF50)
        ConnectionStatus.Disconnected, is ConnectionStatus.Error -> Color(0xFFF44336)
        ConnectionStatus.Connecting -> Color(0xFFFF9800)
    }
    val labelText = when (status) {
        ConnectionStatus.Connected -> "サーバー接続済み"
        ConnectionStatus.Disconnected -> "サーバー未接続"
        ConnectionStatus.Connecting -> "接続中..."
        is ConnectionStatus.Error -> "接続エラー"
    }
    val showButton = status is ConnectionStatus.Disconnected || status is ConnectionStatus.Error

    HorizontalDivider()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(modifier = Modifier.size(8.dp)) {
            drawCircle(color = dotColor)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = labelText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )
        if (showButton) {
            TextButton(
                onClick = onConnect,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            ) {
                Text("接続", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

/** 接続中に全画面を覆うオーバーレイ。 */
@Composable
private fun ConnectingOverlay(onCancel: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .pointerInput(Unit) { detectTapGestures { } }, // タッチイベントを消費して背面操作を防ぐ
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressIndicator(color = Color.White)
            Text(
                "サーバーに接続中...",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                "起動には最大1分かかる場合があります",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onCancel,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.7f)),
            ) {
                Text("キャンセル")
            }
        }
    }
}
