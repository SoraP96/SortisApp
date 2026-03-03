package com.example.banca.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.banca.ui.components.BotonCircular
import com.example.banca.ui.components.BotonCircularGrande
import com.example.banca.ui.components.SeccionSimple
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Estados
    var mensaje by remember { mutableStateOf("Sin acción") }
    val scrollState = rememberScrollState()

    // Control del menú lateral (Drawer)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 1. ESTRUCTURA DEL MENÚ LATERAL (DRAWER)
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp) // Ancho del menú
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Administración",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                // Sección Configuraciones en el menú
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                    label = { Text("Horario") },
                    selected = false,
                    onClick = {
                        mensaje = "Config -> Horario"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    label = { Text("Pagos") },
                    selected = false,
                    onClick = {
                        mensaje = "Config -> Pagos"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Warning, contentDescription = null) },
                    label = { Text("Límites") },
                    selected = false,
                    onClick = {
                        mensaje = "Config -> Límites"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

                // Sección Usuario en el menú
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.VpnKey, contentDescription = null) },
                    label = { Text("Cambiar Clave") },
                    selected = false,
                    onClick = {
                        mensaje = "Usuario -> Clave"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Configuración App") },
                    selected = false,
                    onClick = {
                        mensaje = "Usuario -> Config"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(Modifier.weight(1f)) // Empuja el botón salir hacia abajo

                // Botón Salir
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                    label = { Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error) },
                    selected = false,
                    onClick = {
                        mensaje = "Usuario -> Salir"
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    ) {
        // 2. ESTRUCTURA DE LA PANTALLA PRINCIPAL
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Nombre de usuario", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        // Botón de menú hamburguesa
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        IconButton(onClick = { mensaje = "Notificaciones" }) {
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

                // 🔹 BOTÓN PRINCIPAL: "Vault / Meter jugada"
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BotonCircularGrande(
                        texto = "NUEVA JUGADA",
                        icono = Icons.Default.AddCircle,
                        onClick = { mensaje = "Abriendo Vault..." }
                    )
                }

                // 🔹 SECCIÓN: "Atrasados"
                SeccionSimple(titulo = "Atrasados") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BotonCircular("Fijo", Icons.Default.LooksOne) { mensaje = "Atrasados: Fijo" }
                        BotonCircular("Terminal", Icons.Default.LooksTwo) { mensaje = "Atrasados: Terminal" }
                        BotonCircular("Decena", Icons.Default.Looks3) { mensaje = "Atrasados: Decena" }
                    }
                }

                // 🔹 SECCIÓN: "Mis Listas"
                SeccionSimple(titulo = "Gestión de Listas") {
                    val itemsLista = listOf(
                        "Enviadas" to Icons.Default.Send,
                        "Borradores" to Icons.Default.Drafts,
                        "Guardado" to Icons.Default.Save,
                        "Resumen" to Icons.Default.Assessment,
                        "Fondos" to Icons.Default.AccountBalance
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3), // 3 columnas para que respiren mejor los botones
                        modifier = Modifier.height(200.dp),
                        userScrollEnabled = false,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(itemsLista) { (nombre, icono) ->
                            BotonCircular(
                                texto = nombre,
                                icono = icono,
                                onClick = { mensaje = "Listas: $nombre" }
                            )
                        }
                    }
                }

                // 🔹 SECCIÓN: "Limitados"
                SeccionSimple(titulo = "Limitados") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BotonCircular("Bola", Icons.Default.SportsBasketball) { mensaje = "Limitados: Bola" }
                        BotonCircular("Parte", Icons.Default.PieChart) { mensaje = "Limitados: Parte" }
                    }
                }

                // Estado de prueba
                Text(
                    text = "Acción: $mensaje",
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