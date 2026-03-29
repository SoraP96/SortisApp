package com.example.banca.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.banca.ui.viewmodels.ResultViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ResultScreen(viewModel: ResultViewModel = viewModel()) {

    val pick3 by viewModel.pick3.collectAsState()
    val pick4 by viewModel.pick4.collectAsState()
    val status by viewModel.status.collectAsState()
    val plays by viewModel.plays.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Resultados de Lotería", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = pick3,
            onValueChange = { viewModel.onPick3Change(it) },
            label = { Text("Pick 3") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = pick4,
            onValueChange = { viewModel.onPick4Change(it) },
            label = { Text("Pick 4") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.saveResult() }) {
            Text("Guardar resultado")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.calculatePrizes() }) {
            Text("Calcular premios")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Estado: $status")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.loadResults() }) {
            Text("Ver resultados")
        }

        plays.forEach {
            Text("Num: ${it.playNumber} → Premio: ${it.prize}")
        }
    }

}