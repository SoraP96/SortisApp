package com.example.banca.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.banca.data.database.DatabaseProvider
import com.example.banca.data.repository.PlayRepository
import com.example.banca.domain.models.PlayTicket
import com.example.banca.domain.usecases.CalculateTicketUseCase
import com.example.banca.domain.usecases.CheckLimitsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Cambiamos ViewModel() por AndroidViewModel(application) para tener contexto
class VaultViewModel(application: Application) : AndroidViewModel(application) {

    private val calculateTicketUseCase = CalculateTicketUseCase()
    private val checkLimitsUseCase = CheckLimitsUseCase()

    // NUEVO: Instanciamos el repositorio usando nuestro DatabaseProvider encriptado
    private val repository: PlayRepository

    init {
        val dao = DatabaseProvider.getDatabase(application).playDao()
        repository = PlayRepository(dao)
    }

    private val _numberInput = MutableStateFlow("")
    val numberInput: StateFlow<String> = _numberInput.asStateFlow()

    private val _amountInput = MutableStateFlow("")
    val amountInput: StateFlow<String> = _amountInput.asStateFlow()

    private val _playType = MutableStateFlow("Fijo")
    val playType: StateFlow<String> = _playType.asStateFlow()

    private val _inputPhase = MutableStateFlow(0)
    val inputPhase: StateFlow<Int> = _inputPhase.asStateFlow()

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

        val isAllowed = checkLimitsUseCase.execute(
            number = _numberInput.value,
            playType = _playType.value,
            newAmount = amount
        )

        if (!isAllowed) {
            _errorMessage.value = "¡Límite superado para el número ${_numberInput.value}!"
            _inputPhase.value = 0
            _numberInput.value = ""
            _amountInput.value = ""
            return
        }

        _errorMessage.value = ""

        val ticket = calculateTicketUseCase.execute(
            number = _numberInput.value,
            type = _playType.value,
            amountText = _amountInput.value
        )

        if (ticket != null) {
            // NUEVO: Guardamos en la base de datos usando una corrutina
            viewModelScope.launch {
                val insertedId = repository.savePlay(ticket)
                println("¡ÉXITO! Ticket encriptado y guardado en SQLCipher con el ID: $insertedId")
            }
        }

        _numberInput.value = ""
        _amountInput.value = ""
        _inputPhase.value = 0
    }
}