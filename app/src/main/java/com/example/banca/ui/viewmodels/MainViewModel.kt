package com.example.banca.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _actionMessage = MutableStateFlow("Sin acción")
    val actionMessage: StateFlow<String> = _actionMessage.asStateFlow()

    fun registerAction(newAction: String) {
        _actionMessage.value = newAction
    }

    fun logout() {
        _actionMessage.value = "Cerrando sesión..."
    }
}