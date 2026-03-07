package com.example.banca.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Looks
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toArgb

@Composable
fun SortisAppIcon(size: Dp = 120.dp) {
    val colorDegradado = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant
        )
    )

    Box(
        modifier = Modifier
            .size(size)
            .background(colorDegradado, shape = CircleShape)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Public,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
            modifier = Modifier.size(size * 0.7f)
        )
        Icon(
            imageVector = Icons.Default.Looks,
            contentDescription = "Sortis",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@Composable
fun SeccionSimple(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            contenido()
        }
    }
}

@Composable
fun BotonCircular(
    texto: String,
    icono: ImageVector,
    colorFondo: Color = MaterialTheme.colorScheme.secondaryContainer,
    colorIcono: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = colorFondo,
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icono,
                    contentDescription = texto,
                    tint = colorIcono,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
fun BotonCircularGrande(
    texto: String,
    icono: ImageVector,
    colorFondo: Color = MaterialTheme.colorScheme.primary,
    colorIcono: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = colorFondo,
            modifier = Modifier.size(90.dp),
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icono,
                    contentDescription = texto,
                    tint = colorIcono,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BadgeEstado(
    registrado: Double,
    tope: Double,
    modifier: Modifier = Modifier
) {
    val porcentaje = if (tope > 0) registrado / tope else 0.0
    val colorEstado = when {
        porcentaje >= 1.0 -> MaterialTheme.colorScheme.error // Rojo: Cerrado
        porcentaje >= 0.8 -> Color(0xFFFFA726) // Naranja: Casi lleno
        else -> Color(0xFF66BB6A) // Verde: Abierto
    }

    Surface(
        color = colorEstado,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Text(
            text = if (porcentaje >= 1.0) "CERRADO" else "${(porcentaje * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}