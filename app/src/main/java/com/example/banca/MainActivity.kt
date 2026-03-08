package com.example.banca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.banca.ui.screens.LoginScreen
import com.example.banca.ui.screens.MainScreen
import com.example.banca.ui.screens.LimitsScreen
import com.example.banca.ui.screens.VaultScreen // NUEVO IMPORT

// Enum translated to English
enum class AppScreen {
    Login,
    Dashboard,
    Limits,
    Vault
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {

                var currentScreen by remember { mutableStateOf(AppScreen.Login) }

                BackHandler(enabled = currentScreen != AppScreen.Login) {
                    currentScreen = when (currentScreen) {
                        AppScreen.Limits -> AppScreen.Dashboard
                        AppScreen.Vault -> AppScreen.Dashboard
                        else -> AppScreen.Login
                    }
                }

                Surface(color = MaterialTheme.colorScheme.background) {
                    when (currentScreen) {
                        AppScreen.Login -> {
                            LoginScreen(
                                onLoginSuccess = { currentScreen = AppScreen.Dashboard }
                            )
                        }
                        AppScreen.Dashboard -> {
                            MainScreen(
                                onNavigateToLimits = { currentScreen = AppScreen.Limits },
                                onNavigateToVault = { currentScreen = AppScreen.Vault }
                            )
                        }
                        AppScreen.Limits -> {
                            LimitsScreen(
                                onBack = { currentScreen = AppScreen.Dashboard }
                            )
                        }
                        AppScreen.Vault -> {
                            VaultScreen(
                                onBack = { currentScreen = AppScreen.Dashboard }
                            )
                        }
                    }
                }
            }
        }
    }
}