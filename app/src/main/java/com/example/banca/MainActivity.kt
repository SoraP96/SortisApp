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
import com.example.banca.ui.theme.screens.PantallaLogin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {

                var estaLogueado by remember { mutableStateOf(false) }

                if (estaLogueado) {
                    PantallaListero()
                } else {
                    PantallaLogin(
                        onLoginSuccess = { estaLogueado = true }
                    )
                }
            }
        }
    }
}



@Composable
fun PantallaListero() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pantalla del Listero",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    MaterialTheme {
        PantallaLogin(onLoginSuccess = {})
    }
}