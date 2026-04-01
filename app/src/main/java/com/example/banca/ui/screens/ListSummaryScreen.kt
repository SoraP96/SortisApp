package com.example.banca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banca.ui.viewmodels.ListViewModel
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSummaryScreen(onNavigateToDetail: (Long) -> Unit, viewModel: ListViewModel = viewModel()) {

    val lists by viewModel.lists.collectAsState()
    val shift by viewModel.selectedShift.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    // 🔥 TOTALES REALES desde ViewModel
    val totalAmount by viewModel.totalAmount.collectAsState()
    val totalPrize by viewModel.totalPrize.collectAsState()
    val bankNet by viewModel.bankNet.collectAsState()

    val listeroGain by viewModel.listeroGain.collectAsState()


    LaunchedEffect(selectedDate, shift) {
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

        Button(
            onClick = { showDatePicker = true }
        ) {
            Text("Seleccionar fecha")
        }

        if (showDatePicker) {

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                viewModel.setDate(it)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDatePicker = false }
                    ) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }


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
                Text("Ganancia listero: $listeroGain")
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
                    elevation = CardDefaults.cardElevation(2.dp),
                    onClick = {
                        onNavigateToDetail(list.id)
                    }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            "Lista ID: ${list.id}",
                            fontWeight = FontWeight.Bold
                        )

                        Text("Fecha: ${list.date}")
                        Text("Turno: ${list.shift}")

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("Tap para ver detalle")
                    }
                }
            }
        }
    }
}