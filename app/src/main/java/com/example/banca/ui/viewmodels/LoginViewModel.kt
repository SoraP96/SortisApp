package com.example.banca.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    // Variables privadas: Solo el ViewModel puede modificarlas.
    // Esto es un principio básico de seguridad (Encapsulamiento).
    private val _codigo = MutableStateFlow("")
    val codigo: StateFlow<String> = _codigo.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave: StateFlow<String> = _clave.asStateFlow()

    private val _loginExitoso = MutableStateFlow(false)
    val loginExitoso: StateFlow<Boolean> = _loginExitoso.asStateFlow()

    private val _mensajeError = MutableStateFlow("")
    val mensajeError: StateFlow<String> = _mensajeError.asStateFlow()

    // Funciones que la interfaz puede llamar para actualizar los datos
    fun onCodigoCambio(nuevoCodigo: String) {
        _codigo.value = nuevoCodigo
    }

    fun onClaveCambio(nuevaClave: String) {
        _clave.value = nuevaClave
    }

    // La lógica real de validación
    fun intentarLogin() {
        // AHORA ESTÁ QUEMADO, pero aquí es donde más adelante
        // conectaremos con la base de datos SQLCipher encriptada.
        val codigoCorrecto = "1234"
        val claveCorrecta = "admin"

        if (_codigo.value == codigoCorrecto && _clave.value == claveCorrecta) {
            _loginExitoso.value = true
            _mensajeError.value = ""
        } else {
            _loginExitoso.value = false
            _mensajeError.value = "Código o clave incorrectos"
        }
    }

    fun resetLogin() {
        _loginExitoso.value = false
    }
}