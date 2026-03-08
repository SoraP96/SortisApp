package com.example.banca.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.banca.domain.models.PlayTicket
import com.example.banca.domain.usecases.CalculateTicketUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.banca.domain.usecases.CheckLimitsUseCase

class VaultViewModel : ViewModel() {

    // Instanciamos el motor matemático (Más adelante usaremos Inyección de Dependencias real)
    private val calculateTicketUseCase = CalculateTicketUseCase()
    private val checkLimitsUseCase = CheckLimitsUseCase()

    // Estados de la interfaz
    private val _numberInput = MutableStateFlow("")
    val numberInput: StateFlow<String> = _numberInput.asStateFlow()

    private val _amountInput = MutableStateFlow("")
    val amountInput: StateFlow<String> = _amountInput.asStateFlow()

    private val _playType = MutableStateFlow("Fijo")
    val playType: StateFlow<String> = _playType.asStateFlow()

    private val _inputPhase = MutableStateFlow(0)
    val inputPhase: StateFlow<Int> = _inputPhase.asStateFlow()

    // Variable temporal para ver el ticket generado en la consola (prueba)
    private val _lastTicket = MutableStateFlow<PlayTicket?>(null)
    val lastTicket: StateFlow<PlayTicket?> = _lastTicket.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    fun onKeyPressed(key: String) {
        if (_inputPhase.value == 0) {
            val charLimit = if (_playType.value == "Parle") 4 else 2
            if (_numberInput.value.length < charLimit) {
                _numberInput.value += key
            }
        } else {
            if (_amountInput.value.length < 5) {
                _amountInput.value += key
            }
        }
    }

    fun onDelete() {
        if (_inputPhase.value == 1) {
            if (_amountInput.value.isNotEmpty()) {
                _amountInput.value = _amountInput.value.dropLast(1)
            } else {
                _inputPhase.value = 0
            }
        } else {
            if (_numberInput.value.isNotEmpty()) {
                _numberInput.value = _numberInput.value.dropLast(1)
            }
        }
    }

    fun onNextPhase() {
        if (_inputPhase.value == 0 && _numberInput.value.isNotEmpty()) {
            // Validamos que el Parle tenga 4 números obligatoriamente
            if (_playType.value == "Parle" && _numberInput.value.length < 4) return
            _inputPhase.value = 1
        } else if (_inputPhase.value == 1 && _amountInput.value.isNotEmpty()) {
            registerPlay()
        }
    }

    fun changePlayType(type: String) {
        _playType.value = type
        _numberInput.value = ""
        _inputPhase.value = 0
        _errorMessage.value = ""
    }

    private fun registerPlay() {
        val amount = _amountInput.value.toDoubleOrNull() ?: return

        // 1. Verificamos si la jugada supera el tope del banco
        val isAllowed = checkLimitsUseCase.execute(
            number = _numberInput.value,
            playType = _playType.value,
            newAmount = amount
        )

        if (!isAllowed) {
            // ¡Bloqueamos la jugada y avisamos a la vista!
            _errorMessage.value = "¡Límite superado para el número ${_numberInput.value}!"
            _inputPhase.value = 0 // Lo devolvemos a teclear el número
            _numberInput.value = ""
            _amountInput.value = ""
            return // Detenemos la ejecución aquí
        }

        // Si pasó el límite, limpiamos errores
        _errorMessage.value = ""

        // 2. Enviamos los datos crudos al motor matemático
        val ticket = calculateTicketUseCase.execute(
            number = _numberInput.value,
            type = _playType.value,
            amountText = _amountInput.value
        )

        // 3. Guardamos el ticket
        if (ticket != null) {
            _lastTicket.value = ticket
            println("Ticket generado con éxito: $ticket")
        }

        // 4. Limpiamos la pantalla para la siguiente jugada
        _numberInput.value = ""
        _amountInput.value = ""
        _inputPhase.value = 0
    }
}