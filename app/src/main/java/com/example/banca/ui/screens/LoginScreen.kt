package com.example.banca.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    // Estados
    var codigo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // 1. FONDO DEGRADADO (Púrpura Tirio -> Azul Medianoche)
    val colorDegradado = Brush.linearGradient(
    colors = listOf(
        MaterialTheme.colorScheme.surfaceVariant, // Usamos los slots existentes pero de forma blended
        MaterialTheme.colorScheme.background
    )
    )

    // 2. ANIMACIÓN DEL LOGO (Rotación lenta)
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing), // Rotación muy lenta
            repeatMode = RepeatMode.Restart
        )
    )

    // ESTRUCTURA PRINCIPAL (Fondo)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorDegradado),
        contentAlignment = Alignment.Center
    ) {
        // TARJETA DE LOGIN (Card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // 🔹 LOGO DE SORTIS CON ANIMACIÓN (Símbolo de la 'S' rotando)
                Box(contentAlignment = Alignment.Center) {
                    // Marca de agua sutil (laurel romano)
                    Icon(
                        imageVector = Icons.Default.Public, // Un icono de red/pilar como base
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        modifier = Modifier.size(80.dp)
                    )
                    // La 'S' geométrica y animada
                    Icon(
                        imageVector = Icons.Default.Looks, // Usamos este como 'S' geométrica
                        contentDescription = "Sortis",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(50.dp)
                            .rotate(rotationAngle) // ¡Aplicamos la animación de rotación!
                    )
                }

                // 🔹 TÍTULO Y ESLOGAN
                Text(
                    text = "Sortis",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
                Text(
                    text = "Tu lista, tu banco, tu fortuna.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // 🔹 CAMPOS DE ENTRADA
                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código de Listero") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = clave,
                    onValueChange = { clave = it },
                    label = { Text("Clave de Acceso") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    singleLine = true
                )

                // 🔹 BOTÓN ENTRAR (con sombra)
                Button(
                    onClick = {
                        if (codigo.isNotEmpty() && clave.isNotEmpty()) {
                            onLoginSuccess()
                        }
                    },
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ENTRAR", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Versión al final de la pantalla (fuera de la tarjeta)
        Text(
            text = "Sortis v1.0 | © 2026 ADYDY",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}