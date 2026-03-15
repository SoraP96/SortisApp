package com.example.banca.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.banca.data.database.DatabaseProvider
import com.example.banca.data.repository.PlayRepository
import com.example.banca.data.repository.ListRepository // NUEVO
import com.example.banca.domain.models.PlayTicket
import com.example.banca.domain.usecases.CalculateTicketUseCase
import com.example.banca.domain.usecases.CheckLimitsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Usamos AndroidViewModel para tener acceso al Context (Application)
class VaultViewModel(application: Application) : AndroidViewModel(application) {

    private val calculateTicketUseCase = CalculateTicketUseCase()
    private val checkLimitsUseCase = CheckLimitsUseCase()

    // Repository de jugadas (ya lo tenías)
    private val repository: PlayRepository

    // NUEVO: Repository de listas
    private val listRepository: ListRepository

    init {

        // CAMBIO: obtenemos UNA sola instancia de la base de datos
        val database = DatabaseProvider.getDatabase(application)

        // CAMBIO: obtenemos ambos DAO desde la misma base de datos
        val playDao = database.playDao()
        val listDao = database.listDao()

        // Inicializamos ambos repositorios
        repository = PlayRepository(playDao)

        // NUEVO: inicializamos el repositorio de listas
        listRepository = ListRepository(listDao)
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

            viewModelScope.launch {

                // NUEVO (preparado para el sistema de listas):
                // aquí más adelante obtendremos la lista abierta

                viewModelScope.launch {

                    // 1 buscar lista activa
                    val listId = getOrCreateActiveList()

                    // 2 guardar jugada en esa lista
                    val insertedId = repository.savePlay(ticket, listId)

                    println("Play guardado en lista $listId con id $insertedId")
                }

                //println("¡ÉXITO! Ticket encriptado y guardado en SQLCipher con el ID: $insertedId")

                // EJEMPLO FUTURO (aún no lo usamos):
                // val openList = listRepository.getOpenList()
            }
        }

        _numberInput.value = ""
        _amountInput.value = ""
        _inputPhase.value = 0
    }

    private suspend fun getOrCreateActiveList(): Long {

        val openList = listRepository.getOpenList()

        return if (openList != null) {

            openList.id

        } else {

            // crear nueva lista
            listRepository.createList("DEFAULT")

        }
    }
}
