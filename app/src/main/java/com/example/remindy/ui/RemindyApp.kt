package com.example.remindy.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.remindy.ui.auth.LoginScreen
import com.example.remindy.ui.reminder.ReminderEditScreen
import com.example.remindy.ui.reminder.ReminderListScreen
import com.example.remindy.ui.reminder.ReminderViewModel
import com.example.remindy.ui.settings.SettingsScreen
import com.example.remindy.ui.settings.SettingsViewModel
import com.example.remindy.ui.study.StudyEditScreen
import com.example.remindy.ui.study.StudyListScreen
import com.example.remindy.ui.study.StudyViewModel

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    TabItem("reminders", "リマインダー", Icons.Filled.Notifications),
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
    val reminderVm: ReminderViewModel = viewModel(factory = factory)
    val studyVm: StudyViewModel = viewModel(factory = factory)
    val settingsVm: SettingsViewModel = viewModel(factory = factory)

    Scaffold(
        bottomBar = {
            val backStack by navController.currentBackStackEntryAsState()
            val current = backStack?.destination
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
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
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "reminders",
            modifier = Modifier.padding(padding),
        ) {
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
                SettingsScreen(viewModel = settingsVm)
            }
        }
    }
}
