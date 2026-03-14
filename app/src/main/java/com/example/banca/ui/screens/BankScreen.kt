package com.example.banca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankScreen(onBack: () -> Unit) {
    var totalFunds by remember { mutableStateOf("") }
    var maxFijo by remember { mutableStateOf("") }
    var maxParle by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel del Banco", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Regresar") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    println("Guardando: Fondos=$totalFunds, TopeFijo=$maxFijo, TopeParle=$maxParle")
                    onBack()
                },
                icon = { Icon(Icons.Default.Save, contentDescription = "Guardar") },
                text = { Text("Guardar Cambios") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Fondos Disponibles ($)", fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = totalFunds, onValueChange = { totalFunds = it }, modifier = Modifier.fillMaxWidth())
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Límites de Riesgo Mínimos ($)", fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = maxFijo, onValueChange = { maxFijo = it }, label = { Text("Fijo") }, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = maxParle, onValueChange = { maxParle = it }, label = { Text("Parle") }, modifier = Modifier.fillMaxWidth())
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}