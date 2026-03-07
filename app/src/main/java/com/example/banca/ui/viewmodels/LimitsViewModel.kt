package com.example.banca.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Estructuras de datos (Modelos)
data class BolaLimit(val numero: String, val registrado: Double, val tope: Double)
data class ParleLimit(val num1: String, val num2: String, val registrado: Double, val tope: Double)

class LimitsViewModel : ViewModel() {

    // 2. Variables privadas (El estado interno)
    private val _bolaLimits = MutableStateFlow<List<BolaLimit>>(emptyList())
    val bolaLimits: StateFlow<List<BolaLimit>> = _bolaLimits.asStateFlow()

    private val _parleLimits = MutableStateFlow<List<ParleLimit>>(emptyList())
    val parleLimits: StateFlow<List<ParleLimit>> = _parleLimits.asStateFlow()

    // init se ejecuta automáticamente cuando se abre la pantalla
    init {
        cargarLimites()
    }

    // 3. Lógica para obtener los datos
    private fun cargarLimites() {
        // 🚀 Nota: Aquí es donde haremos la consulta (SELECT) a la base de datos SQLCipher.
        // Por ahora, inyectamos los datos de prueba desde el "cerebro".

        _bolaLimits.value = listOf(
            BolaLimit("14", 450.0, 500.0),
            BolaLimit("23", 490.0, 500.0),
            BolaLimit("07", 500.0, 500.0), // Cerrado
            BolaLimit("45", 120.0, 300.0),
            BolaLimit("88", 290.0, 300.0)
        )

        _parleLimits.value = listOf(
            ParleLimit("03", "34", 1500.0, 1500.0), // Cerrado
            ParleLimit("05", "30", 1200.0, 1500.0),
            ParleLimit("24", "91", 450.0, 1000.0)
        )
    }
}