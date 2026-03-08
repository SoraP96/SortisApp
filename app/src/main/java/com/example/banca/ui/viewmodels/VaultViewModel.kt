package com.example.banca.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VaultViewModel : ViewModel() {

    // Lo que el listero está escribiendo en este momento
    private val _numeroInput = MutableStateFlow("")
    val numeroInput: StateFlow<String> = _numeroInput.asStateFlow()

    private val _montoInput = MutableStateFlow("")
    val montoInput: StateFlow<String> = _montoInput.asStateFlow()

    private val _tipoJugada = MutableStateFlow("Fijo") // Por defecto
    val tipoJugada: StateFlow<String> = _tipoJugada.asStateFlow()

    // Fases de escritura: 0 = Escribiendo número, 1 = Escribiendo monto
    private val _faseInput = MutableStateFlow(0)
    val faseInput: StateFlow<Int> = _faseInput.asStateFlow()

    // Eventos del Teclado Numérico
    fun onTeclaPresionada(tecla: String) {
        if (_faseInput.value == 0) {
            // Escribiendo número (máximo 2 dígitos para Fijo/Corrido, 4 para Parle)
            val limiteCaracteres = if (_tipoJugada.value == "Parle") 4 else 2
            if (_numeroInput.value.length < limiteCaracteres) {
                _numeroInput.value += tecla
            }
        } else {
            // Escribiendo monto (dinero)
            if (_montoInput.value.length < 5) { // Límite de $99,999 para no desbordar
                _montoInput.value += tecla
            }
        }
    }

    fun onBorrar() {
        if (_faseInput.value == 1) {
            if (_montoInput.value.isNotEmpty()) {
                _montoInput.value = _montoInput.value.dropLast(1)
            } else {
                _faseInput.value = 0 // Vuelve a borrar el número
            }
        } else {
            if (_numeroInput.value.isNotEmpty()) {
                _numeroInput.value = _numeroInput.value.dropLast(1)
            }
        }
    }

    fun onSiguienteFase() {
        if (_faseInput.value == 0 && _numeroInput.value.isNotEmpty()) {
            _faseInput.value = 1 // Pasa a pedir el dinero
        } else if (_faseInput.value == 1 && _montoInput.value.isNotEmpty()) {
            registrarJugada() // ¡Enter final!
        }
    }

    fun cambiarTipoJugada(tipo: String) {
        _tipoJugada.value = tipo
        _numeroInput.value = "" // Resetea si cambia de tipo para evitar errores
        _faseInput.value = 0
    }

    private fun registrarJugada() {
        // AQUÍ CONECTAREMOS CON LA BASE DE DATOS EN LA FASE 3
        // Por ahora, solo limpiamos la pantalla para la siguiente jugada
        _numeroInput.value = ""
        _montoInput.value = ""
        _faseInput.value = 0
    }
}