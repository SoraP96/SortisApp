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
import com.example.banca.domain.utils.PdfExporter
import android.content.Intent
import android.widget.Toast
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.io.FileInputStream

@Composable
fun ListDetailScreen(
    listId: Long,
    selectedDate: Long,
    viewModel: ListDetailViewModel = viewModel()
) {

    val plays by viewModel.plays.collectAsState()
    var isEditable by remember { mutableStateOf(false) }

    LaunchedEffect(listId) {
        isEditable = viewModel.isCurrentListEditable(listId)
    }
    val context = LocalContext.current
    val savePdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->

        uri?.let {
            val tempFile = generarPdfConResumen(
                context,
                listId,
                plays
            )

            context.contentResolver.openOutputStream(uri)?.use { output ->
                FileInputStream(tempFile).use { input ->
                    input.copyTo(output)
                }
            }

            Toast.makeText(
                context,
                "PDF guardado correctamente",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    var expandedMenu by remember { mutableStateOf(false) }

    LaunchedEffect(listId, selectedDate) {
        viewModel.loadPlays(listId)
        viewModel.loadResultForDate(selectedDate)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Detalle Lista $listId",
                style = MaterialTheme.typography.headlineMedium
            )

            Box {
                IconButton(
                    onClick = { expandedMenu = true }
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Menú"
                    )
                }

                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Exportar PDF") },
                        onClick = {
                            expandedMenu = false

                            val file = generarPdfConResumen(
                                context,
                                listId,
                                plays
                            )

                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                file
                            )

                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }

                            context.startActivity(
                                Intent.createChooser(intent, "Compartir PDF")
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Guardar como...") },
                        onClick = {
                            expandedMenu = false

                            savePdfLauncher.launch("Lista_$listId.pdf")

                            Toast.makeText(
                                context,
                                "PDF guardado en el teléfono",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 números ganadores (temporal)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    "Tiradas",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val result by viewModel.result.collectAsState()

                if (result != null) {
                    Text("Pick3: ${result?.pick3}")
                    Text("Pick4: ${result?.pick4}")
                }

                Spacer(modifier = Modifier.height(12.dp))

                val totalPlayed = plays.sumOf { it.amount }
                val totalPrizes = plays.sumOf { it.prize ?: 0.0 }
                val bankNet = plays.sumOf {
                    it.bankCleanMoney - (it.prize ?: 0.0)
                }
                val listeroNet = plays.sumOf { it.listeroCut }

                Text("Total jugado: ${"%.0f".format(totalPlayed)}")
                Text("Premios: ${"%.0f".format(totalPrizes)}")
                Text("Ganancia Banco: ${"%.0f".format(bankNet)}")
                Text("Ganacia Listero: ${"%.0f".format(listeroNet)}")
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
                        Text("Jugado: ${"%.0f".format(play.amount)}")

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

                        if (isEditable) {
                            Button(
                                onClick = {
                                    showDeleteDialog = true
                                }
                            ) {
                                Text("Eliminar")
                            }
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

private fun generarPdfConResumen(
    context: android.content.Context,
    listId: Long,
    plays: List<com.example.banca.data.entities.PlayEntity>
): java.io.File {

    val totalPlayed = plays.sumOf { it.amount }
    val totalPrizes = plays.sumOf { it.prize ?: 0.0 }
    val bankNet = plays.sumOf {
        it.bankCleanMoney - (it.prize ?: 0.0)
    }
    val listeroNet = plays.sumOf { it.listeroCut }

    return PdfExporter.exportarLista(
        context = context,
        titulo = "Lista_$listId",
        elementos = plays.map {
            "${it.playNumber} - ${it.playType} - ${it.amount}"
        },
        totalJugado = totalPlayed,
        premios = totalPrizes,
        banco = bankNet,
        listero = listeroNet
    )
}