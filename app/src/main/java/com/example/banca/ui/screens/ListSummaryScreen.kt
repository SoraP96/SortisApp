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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.banca.domain.utils.BackupManager
import com.example.banca.data.database.DatabaseProvider
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSummaryScreen(onNavigateToDetail: (Long) -> Unit, viewModel: ListViewModel = viewModel()) {

    val lists by viewModel.lists.collectAsState()
    val shift by viewModel.selectedShift.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val backupLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument(
            "application/octet-stream"
        )
    ) { uri ->
        uri?.let {
            BackupManager.exportPortableBackup(context, it)

            scope.launch {
                snackbarHostState.showSnackbar(
                    "✔ Backup exportado correctamente"
                )
            }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            BackupManager.restorePortableBackup(context, it)
            DatabaseProvider.resetDatabaseInstance()
            viewModel.loadLists(selectedDate)

            scope.launch {
                snackbarHostState.showSnackbar(
                    "✔ Backup restaurado correctamente"
                )
            }
        }
    }

    // 🔥 TOTALES REALES desde ViewModel
    val totalAmount by viewModel.totalAmount.collectAsState()
    val totalPrize by viewModel.totalPrize.collectAsState()
    val bankNet by viewModel.bankNet.collectAsState()

    val listeroGain by viewModel.listeroGain.collectAsState()


    val formattedDate = remember(selectedDate) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Date(selectedDate))
    }

    LaunchedEffect(selectedDate, shift) {
        viewModel.loadLists(selectedDate)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text("Listas", style = MaterialTheme.typography.headlineMedium)


            Spacer(modifier = Modifier.height(8.dp))

            // 📅 FECHA
            Text("Fecha actual", fontWeight = FontWeight.Bold)

            Button(
                onClick = { showDatePicker = true }
            ) {
                Text("$formattedDate")
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
                    onClick = { viewModel.setShift("Mañana") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (shift == "Mañana")
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Día")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { viewModel.setShift("Noche") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            if (shift == "Noche")
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

                    Text("Total jugado: ${"%.0f".format(totalAmount)}")
                    Text("Premios: ${"%.0f".format(totalPrize)}")
                    Text("Ganancia listero: ${"%.0f".format(listeroGain)}")
                    Text("Ganancia Banco: ${"%.0f".format(bankNet)}")
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

                            Text("Fecha: ${formattedDate}")
                            Text("Turno: ${list.shift}")

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("Tap para ver detalle")
                        }
                    }
                }
            }
            Button(
                enabled = lists.isNotEmpty(),
                onClick = {
                    viewModel.closeCurrentOpenLists()

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "✔ Lista cerrada correctamente",
                            withDismissAction = false
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar lista")
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                enabled = lists.isNotEmpty(),
                onClick = {
                    backupLauncher.launch("banca_backup_${formattedDate.replace("/", "_")}.db")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Exportar backup")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    restoreLauncher.launch(arrayOf("*/*"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Restaurar backup")
            }
        }
    }
}