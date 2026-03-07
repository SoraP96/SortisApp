package com.example.banca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // NUEVO IMPORT
import com.example.banca.ui.components.BotonCircular
import com.example.banca.ui.components.BotonCircularGrande
import com.example.banca.ui.components.SeccionSimple
import com.example.banca.ui.viewmodels.MainViewModel // NUEVO IMPORT
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(), // Inyectamos el cerebro
    onNavigateToLimits: () -> Unit
) {
    // 🧠 Observamos los datos que vienen del ViewModel
    val mensaje by viewModel.mensajeAccion.collectAsState()

    // 🎨 Estados puramente visuales (se quedan aquí)
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Administración",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                    label = { Text("Horario") },
                    selected = false,
                    onClick = {
                        viewModel.registrarAccion("Config -> Horario")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    label = { Text("Pagos") },
                    selected = false,
                    onClick = {
                        viewModel.registrarAccion("Config -> Pagos")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = null) },
                    label = { Text("Límites generales") },
                    selected = false,
                    onClick = {
                        viewModel.registrarAccion("Config -> Límites")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.VpnKey, contentDescription = null) },
                    label = { Text("Cambiar Clave") },
                    selected = false,
                    onClick = {
                        viewModel.registrarAccion("Usuario -> Clave")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Configuración App") },
                    selected = false,
                    onClick = {
                        viewModel.registrarAccion("Usuario -> Config")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(Modifier.weight(1f))

                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                    label = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                    selected = false,
                    onClick = {
                        viewModel.cerrarSesion()
                        scope.launch { drawerState.close() }
                        // Aquí en el futuro llamaremos a una función para volver al Login
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Sortis", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.registrarAccion("Notificaciones abiertas") }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BotonCircularGrande(
                        texto = "NUEVA JUGADA",
                        icono = Icons.Default.AddCircle,
                        onClick = { viewModel.registrarAccion("Abriendo Vault...") }
                    )
                }

                SeccionSimple(titulo = "Atrasados") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BotonCircular("Fijo", Icons.Default.LooksOne) { viewModel.registrarAccion("Atrasados: Fijo") }
                        BotonCircular("Terminal", Icons.Default.LooksTwo) { viewModel.registrarAccion("Atrasados: Terminal") }
                        BotonCircular("Decena", Icons.Default.Looks3) { viewModel.registrarAccion("Atrasados: Decena") }
                    }
                }

                SeccionSimple(titulo = "Gestión de Listas") {
                    val itemsLista = listOf(
                        "Enviadas" to Icons.Default.Send,
                        "Borradores" to Icons.Default.Drafts,
                        "Guardado" to Icons.Default.Save,
                        "Resumen" to Icons.Default.Assessment,
                        "Fondos" to Icons.Default.AccountBalance
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.height(200.dp),
                        userScrollEnabled = false,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(itemsLista) { (nombre, icono) ->
                            BotonCircular(
                                texto = nombre,
                                icono = icono,
                                onClick = { viewModel.registrarAccion("Listas: $nombre") }
                            )
                        }
                    }
                }

                SeccionSimple(titulo = "Control de Riesgo") {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        onClick = onNavigateToLimits
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Ver Números Limitados",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "Revisa topes activos de Bola y Parle",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.PieChart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Acción registrada en ViewModel: $mensaje",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}