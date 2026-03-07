package com.example.banca.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    // Variable privada y segura
    private val _mensajeAccion = MutableStateFlow("Sin acción")
    // Lo que la vista (MainScreen) tiene permitido leer
    val mensajeAccion: StateFlow<String> = _mensajeAccion.asStateFlow()

    // Función que llamará la interfaz cuando el usuario toque un botón
    fun registrarAccion(nuevaAccion: String) {
        _mensajeAccion.value = nuevaAccion

        // 🚀 Nota para el futuro:
        // ¡Aquí es exactamente donde escribiremos el código para
        // guardar la jugada en la base de datos SQLCipher encriptada!
    }

    fun cerrarSesion() {
        // Aquí limpiaremos los datos temporales del listero
        _mensajeAccion.value = "Cerrando sesión..."
    }
}