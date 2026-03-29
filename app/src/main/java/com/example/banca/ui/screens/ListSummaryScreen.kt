package com.example.banca.ui.screens

import com.example.banca.ui.viewmodels.ListViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ListSummaryScreen(viewModel: ListViewModel = viewModel()) {

    val lists by viewModel.lists.collectAsState()
    val shift by viewModel.selectedShift.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Listas", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = { viewModel.setShift("DAY") }) {
                Text("Día")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.setShift("NIGHT") }) {
                Text("Noche")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        lists.forEach { list ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {

                Column(modifier = Modifier.padding(12.dp)) {

                    Text("Lista ID: ${list.id}")

                    Text("Total: ${list.totalAmount}")
                    Text("Premios: ${list.listeroPayment}")
                    Text("Banco: ${list.bankNet}")
                }
            }
        }
    }
}