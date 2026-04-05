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
import com.example.banca.domain.utils.PlayParser
import com.example.banca.domain.utils.ShiftUtils


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

    private val _currentShift = MutableStateFlow(
        ShiftUtils.getCurrentShift()
    )
    val currentShift: StateFlow<String> =
        _currentShift.asStateFlow()

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

    fun refreshCurrentShift() {
        _currentShift.value = ShiftUtils.getCurrentShift()
    }

    fun onKeyPressed(key: String) {
        if (_inputPhase.value == 0) {
            val charLimit = when (_playType.value) {
                "Parle" -> 4
                "Centena" -> 3
                "Fijo" -> 2
                "Corrido" -> 2
                else -> 2
            }
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

            val requiredLength = when (_playType.value) {
                "Parle" -> 4
                "Centena" -> 3
                else -> 2
            }

            if (_numberInput.value.length < requiredLength) return

            _inputPhase.value = 1

        } else if (_inputPhase.value == 1 &&
            _amountInput.value.isNotEmpty()
        ) {
            registerPlay()
        }
    }

    fun changePlayType(type: String) {
        _playType.value = type
        _numberInput.value = ""
        _inputPhase.value = 0
        _errorMessage.value = ""
    }

    private fun getStartOfDay(date: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfDay(date: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23)
        cal.set(java.util.Calendar.MINUTE, 59)
        cal.set(java.util.Calendar.SECOND, 59)
        cal.set(java.util.Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    private fun registerPlay() {

        val amount = _amountInput.value.toDoubleOrNull() ?: return

        if (ShiftUtils.isBettingLocked()) {
            _errorMessage.value = "Las jugadas están bloqueadas"
            return
        }

        viewModelScope.launch {

            val now = System.currentTimeMillis()

            val currentTotal = repository
                .getTodayTotalByNumberAndType(
                    number = _numberInput.value,
                    playType = _playType.value.uppercase(),
                    startOfDay = getStartOfDay(now),
                    endOfDay = getEndOfDay(now)
                )

            val isAllowed = checkLimitsUseCase.execute(
                currentTotal = currentTotal,
                playType = _playType.value,
                newAmount = amount
            )

            if (!isAllowed) {
                _errorMessage.value =
                    "¡Límite superado para ${_numberInput.value}!"
                _inputPhase.value = 0
                _numberInput.value = ""
                _amountInput.value = ""
                return@launch
            }

            continueRegisterPlay()
        }
    }

    private fun continueRegisterPlay() {

        _errorMessage.value = ""

        val ticket = calculateTicketUseCase.execute(
            number = _numberInput.value,
            type = _playType.value,
            amountText = _amountInput.value
        )

        if (ticket != null) {
            viewModelScope.launch {

                val listId = getOrCreateActiveList()

                repository.savePlay(ticket, listId)
            }
        }

        _numberInput.value = ""
        _amountInput.value = ""
        _inputPhase.value = 0
    }

    private suspend fun getOrCreateActiveList(): Long {

        val currentDate = System.currentTimeMillis()
        val currentShift = ShiftUtils.getCurrentShift()

        val startOfDay = getStartOfDay(currentDate)
        val endOfDay = getEndOfDay(currentDate)

        val todayLists = listRepository.getListsByDateAndShift(
            start = startOfDay,
            end = endOfDay,
            shift = currentShift
        )

        return if (todayLists.isNotEmpty()) {

            // usar la lista del día actual
            todayLists.first().id

        } else {

            // crear nueva lista del día
            listRepository.createList(
                listeroCode = "DEFAULT",
                shift = currentShift
            )
        }
    }

}
