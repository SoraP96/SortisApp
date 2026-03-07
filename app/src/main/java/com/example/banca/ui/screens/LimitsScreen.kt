package com.example.banca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // NUEVO IMPORT
import com.example.banca.ui.components.BadgeEstado
// Importamos los modelos y el ViewModel
import com.example.banca.ui.viewmodels.BolaLimit
import com.example.banca.ui.viewmodels.LimitsViewModel
import com.example.banca.ui.viewmodels.ParleLimit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LimitsScreen(
    viewModel: LimitsViewModel = viewModel(), // Inyectamos el cerebro
    onBack: () -> Unit
) {
    // 🧠 Observamos las listas en tiempo real desde el ViewModel
    val bolaLimits by viewModel.bolaLimits.collectAsState()
    val parleLimits by viewModel.parleLimits.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Números Limitados", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Lógica de búsqueda futura */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // 🔹 SECCIÓN 1: BOLA
            item {
                Text(
                    text = "Límites: Bola (Individuales)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                SeccionBolaGrid(bolaLimits) // Pasamos los datos dinámicos
            }

            // 🔹 SECCIÓN 2: PARLE
            item {
                Text(
                    text = "Límites: Parle (Combinaciones)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(parleLimits) { parle -> // Usamos los datos dinámicos
                CardParle(parle)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// 🎨 COMPONENTES DE DISEÑO

@Composable
fun SeccionBolaGrid(limites: List<BolaLimit>) {
    val filas = if (limites.isEmpty()) 0 else (limites.size + 3) / 4
    val alturaGrid = (filas * 90).dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.height(alturaGrid),
        userScrollEnabled = false,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(limites) { bola ->
            CardBolaSmall(bola)
        }
    }
}

@Composable
fun CardBolaSmall(bola: BolaLimit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = bola.numero,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            BadgeEstado(
                registrado = bola.registrado,
                tope = bola.tope,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun CardParle(parle: ParleLimit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = parle.num1,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = " - ",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Text(
                        text = parle.num2,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Registrado: $${parle.registrado.toInt()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                BadgeEstado(registrado = parle.registrado, tope = parle.tope)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tope: $${parle.tope.toInt()}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}