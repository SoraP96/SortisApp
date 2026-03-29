package com.example.banca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banca.ui.viewmodels.ListViewModel

@Composable
fun ListSummaryScreen(viewModel: ListViewModel = viewModel()) {

    val lists by viewModel.lists.collectAsState()
    val shift by viewModel.selectedShift.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    // 🔥 TOTALES REALES desde ViewModel
    val totalAmount by viewModel.totalAmount.collectAsState()
    val totalPrize by viewModel.totalPrize.collectAsState()
    val bankNet by viewModel.bankNet.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLists(selectedDate)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Listas", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // 📅 FECHA
        Text("Fecha actual", fontWeight = FontWeight.Bold)
        Text(selectedDate.toString()) // luego lo pondremos bonito

        Spacer(modifier = Modifier.height(8.dp))

        // 🌙 TURNO
        Row {
            Button(
                onClick = { viewModel.setShift("DAY") },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (shift == "DAY")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Día")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.setShift("NIGHT") },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (shift == "NIGHT")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Noche")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RESUMEN REAL
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                Text("Resumen", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(4.dp))

                Text("Total jugado: $totalAmount")
                Text("Premios: $totalPrize")
                Text("Banco: $bankNet")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 📋 LISTAS
        if (lists.isEmpty()) {
            Text("No hay listas para esta fecha")
        } else {
            lists.forEach { list ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            "Lista ID: ${list.id}",
                            fontWeight = FontWeight.Bold
                        )

                        // 🔥 datos informativos de la lista
                        Text("Fecha: ${list.date}")
                        Text("Turno: ${list.shift}")
                    }
                }
            }
        }
    }
}