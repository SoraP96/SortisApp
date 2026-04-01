package com.example.banca.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banca.ui.viewmodels.ListDetailViewModel

@Composable
fun ListDetailScreen(
    listId: Long,
    viewModel: ListDetailViewModel = viewModel()
) {

    val plays by viewModel.plays.collectAsState()

    LaunchedEffect(listId) {
        viewModel.loadPlays(listId)
    }

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
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Números Lotto", fontWeight = FontWeight.Bold)
                Text("Pick3: 260")
                Text("Pick4: 2221")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(plays) { play ->

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
                    }
                }
            }
        }
    }
}