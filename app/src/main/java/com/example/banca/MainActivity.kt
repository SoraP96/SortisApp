package com.example.banca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.banca.ui.screens.LoginScreen
import com.example.banca.ui.screens.MainScreen
import androidx.activity.compose.BackHandler // Importante para manejar el botón de 'atrás' del móvil
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.banca.ui.screens.LoginScreen
import com.example.banca.ui.screens.MainScreen
import com.example.banca.ui.screens.LimitsScreen

// Definimos los posibles estados de la aplicación
enum class AppScreen {
    Login,
    Dashboard,
    Limits
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                // Estado que controla qué pantalla estamos viendo
                var currentScreen by remember { mutableStateOf(AppScreen.Login) }

                // Manejador del botón 'atrás' físico del teléfono
                BackHandler(enabled = currentScreen != AppScreen.Login) {
                    currentScreen = when (currentScreen) {
                        AppScreen.Limits -> AppScreen.Dashboard
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
                                // Pasamos la función para ir a límites
                                onNavigateToLimits = { currentScreen = AppScreen.Limits }
                            )
                        }
                        AppScreen.Limits -> {
                            LimitsScreen(
                                // Pasamos la función para volver al dashboard
                                onBack = { currentScreen = AppScreen.Dashboard }
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    MaterialTheme {
        LoginScreen(onLoginSuccess = {})
    }
}