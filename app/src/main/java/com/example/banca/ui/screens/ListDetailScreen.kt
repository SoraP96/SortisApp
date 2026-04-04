package com.example.banca.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banca.ui.viewmodels.ListDetailViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf

@Composable
fun ListDetailScreen(
    listId: Long,
    viewModel: ListDetailViewModel = viewModel()
) {

    val plays by viewModel.plays.collectAsState()

    LaunchedEffect(listId) {
        viewModel.loadPlays(listId)
    }
    //Para exportar a PDF
/*
    IconButton(
        onClick = {
            viewModel.exportListPdf(listId)
        }
    ) {
        Icon(Icons.Default.PictureAsPdf, null)
    }
    */


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Detalle Lista $listId",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 números ganadores (temporal)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    "Resultados Lotto",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Pick3: 260")
                Text("Pick4: 2221")

                Spacer(modifier = Modifier.height(12.dp))

                val totalPlayed = plays.sumOf { it.amount }
                val totalPrizes = plays.sumOf { it.prize ?: 0.0 }
                val bankNet = plays.sumOf {
                    it.bankCleanMoney - (it.prize ?: 0.0)
                }
                val listeroNet = plays.sumOf { it.listeroCut }

                Text("Total jugado: $totalPlayed")
                Text("Premios: $totalPrizes")
                Text("Banco: $bankNet")
                Text("Listero: $listeroNet")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(plays) { play ->
                var showDeleteDialog by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if ((play.prize ?: 0.0) > 0)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            text = "Número: ${play.playNumber}",
                            fontWeight = FontWeight.Bold
                        )

                        Text("Tipo: ${play.playType}")
                        Text("Monto: ${play.amount}")

                        val prize = play.prize ?: 0.0

                        if (prize > 0) {
                            Text(
                                text = "🏆 Premio: $prize",
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text("Sin premio")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                showDeleteDialog = true
                            }
                        ) {
                            Text("Eliminar")
                        }
                        if (showDeleteDialog) {
                            AlertDialog(
                                onDismissRequest = {
                                    showDeleteDialog = false
                                },
                                title = {
                                    Text("Confirmar eliminación")
                                },
                                text = {
                                    Text("¿Seguro que deseas eliminar esta jugada?")
                                },
                                confirmButton = {
                                    Button(
                                        onClick = {
                                            viewModel.deletePlay(play.id, listId)
                                            showDeleteDialog = false
                                        }
                                    ) {
                                        Text("Sí, eliminar")
                                    }
                                },
                                dismissButton = {
                                    OutlinedButton(
                                        onClick = {
                                            showDeleteDialog = false
                                        }
                                    ) {
                                        Text("Cancelar")
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