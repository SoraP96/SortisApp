package com.example.banca.ui.screens

// ... (tus imports anteriores)
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // NUEVO IMPORT
import com.example.banca.ui.components.SortisAppIcon
import com.example.banca.ui.viewmodels.LoginViewModel // NUEVO IMPORT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    // Inyectamos el ViewModel
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    // Observamos los datos seguros que vienen del ViewModel
    val codigo by viewModel.codigo.collectAsState()
    val clave by viewModel.clave.collectAsState()
    val loginExitoso by viewModel.loginExitoso.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    // Reaccionamos si el login fue exitoso
    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            onLoginSuccess()
            viewModel.resetLogin() // Limpiamos para cuando cierre sesión
        }
    }

    val colorDegradado = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = Modifier.fillMaxSize().background(colorDegradado),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(24.dp).wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                SortisAppIcon(size = 90.dp)

                Text(
                    text = "Sortis",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                )
                Text(
                    text = "Tu lista, tu banco, tu fortuna.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Mostramos error si lo hay
                if (mensajeError.isNotEmpty()) {
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = codigo,
                    onValueChange = { viewModel.onCodigoCambio(it) }, // El ViewModel maneja el cambio
                    label = { Text("Código de Listero") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = clave,
                    onValueChange = { viewModel.onClaveCambio(it) }, // El ViewModel maneja el cambio
                    label = { Text("Clave de Acceso") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    singleLine = true
                )

                Button(
                    onClick = { viewModel.intentarLogin() }, // Delegamos la validación
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(imageVector = Icons.Default.Login, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ENTRAR", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}