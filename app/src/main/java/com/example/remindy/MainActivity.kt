package com.example.remindy

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.remindy.di.AppContainer
import com.example.remindy.ui.RemindyApp
import com.example.remindy.ui.RemindyViewModelFactory

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val container: AppContainer = (application as RemindyApplication).container
        val factory = RemindyViewModelFactory(container)

        setContent {
            MaterialTheme {
                Surface {
                    RemindyApp(factory = factory, authRepository = container.authRepository)
                }
            }
        }
    }
}
