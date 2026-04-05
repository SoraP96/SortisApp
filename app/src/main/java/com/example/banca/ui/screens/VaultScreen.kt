package com.example.banca.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banca.ui.viewmodels.VaultViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(
    viewModel: VaultViewModel = viewModel(),
    onBack: () -> Unit
) {
    val numero by viewModel.numberInput.collectAsState()
    val monto by viewModel.amountInput.collectAsState()
    val tipo by viewModel.playType.collectAsState()
    val fase by viewModel.inputPhase.collectAsState()
    val error by viewModel.errorMessage.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NUEVA JUGADA", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 1. ZONA DE VISOR (DISPLAY)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF1E1E24)), // Fondo oscuro estilo pantalla LED
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = tipo.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Muestra el número
                        Text(
                            text = if (numero.isEmpty()) "---" else numero,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (fase == 0) Color.White else Color.Gray
                        )
                        Text(
                            text = "  $  ",
                            fontSize = 32.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        // Muestra el monto
                        Text(
                            text = if (monto.isEmpty()) "0" else monto,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (fase == 1) Color.Green else Color.Gray
                        )
                    }
                }
            }
            // ALERTA DE ERROR DE LÍMITES
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                // Espaciador para que la UI no salte cuando no hay error
                Spacer(modifier = Modifier.height(36.dp))
            }

            // 2. SELECTORES DE TIPO DE JUGADA
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BotonTipoJugada("Fijo", tipo == "Fijo") { viewModel.changePlayType("Fijo") }
                BotonTipoJugada("Corrido", tipo == "Corrido") { viewModel.changePlayType("Corrido") }
                BotonTipoJugada("Parle", tipo == "Parle") { viewModel.changePlayType("Parle") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val currentShift by viewModel.currentShift.collectAsState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.Center
            ){
                LaunchedEffect(Unit) {
                    viewModel.refreshCurrentShift()
                }

                Text(
                    text = if (currentShift == "Mañana")
                        "🌞 Tirada de mañana"
                    else
                        "🌙 Tirada de tarde",
                    style = MaterialTheme.typography.titleLarge
                )
            }



            // 3. TECLADO NUMÉRICO GIGANTE
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val filas = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("DEL", "0", "OK")
                )

                for (fila in filas) {
                Row(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (tecla in fila) {
                        TeclaNumerica(
                            texto = tecla,
                            modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp),
                            onClick = {
                                when (tecla) {
                                    "DEL" -> viewModel.onDelete()
                                    "OK" -> viewModel.onNextPhase()
                                    else -> viewModel.onKeyPressed(tecla)
                                }
                            }
                        )
                    }
                }
            }
            }
        }
    }
}

// COMPONENTES AUXILIARES DEL TECLADO

@Composable
fun BotonTipoJugada(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(50)
    ) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun TeclaNumerica(texto: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val isAction = texto == "DEL" || texto == "OK"
    val bgColor = when (texto) {
        "DEL" -> MaterialTheme.colorScheme.errorContainer
        "OK" -> Color(0xFF4CAF50) // Verde para confirmar
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (texto == "OK") Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (texto == "DEL") {
                Icon(Icons.Default.Backspace, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.size(32.dp))
            } else if (texto == "OK") {
                Icon(Icons.Default.Check, contentDescription = "Aceptar", tint = Color.White, modifier = Modifier.size(40.dp))
            } else {
                Text(
                    text = texto,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
        }
    }
}