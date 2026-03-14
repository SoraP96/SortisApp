package com.example.banca.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    // Variables privadas: Solo el ViewModel puede modificarlas.
    // Esto es un principio básico de seguridad (Encapsulamiento).

    private val _userRole = kotlinx.coroutines.flow.MutableStateFlow("LISTERO")

    val userRole: kotlinx.coroutines.flow.StateFlow<String> = _userRole
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
        val user = codigo.value
        val pass = clave.value

        if (user == "admin" && pass == "admin123") {
            _userRole.value = "ADMIN"
            _loginExitoso.value = true
        } else if (user == "listero" && pass == "123") {
            _userRole.value = "LISTERO"
            _loginExitoso.value = true
        } else {
            _mensajeError.value = "Credenciales incorrectas"
        }
    }

    fun resetLogin() {
        _loginExitoso.value = false
    }
}