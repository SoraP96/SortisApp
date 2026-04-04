package com.example.banca.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.banca.data.database.DatabaseProvider
import com.example.banca.data.entities.ListEntity
import com.example.banca.data.repository.ListRepository
import com.example.banca.data.repository.PlayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class ListViewModel(application: Application) : AndroidViewModel(application) {

    // Repository de listas
    private val listRepository: ListRepository

    // 🔥 NUEVO: repository de jugadas para calcular totales reales
    private val playRepository: PlayRepository

    init {
        val db = DatabaseProvider.getDatabase(application)

        listRepository = ListRepository(db.listDao())

        // 🔥 NUEVO
        playRepository = PlayRepository(db.playDao())
    }

    // =========================
    // LISTAS
    // =========================
    private val _lists = MutableStateFlow<List<ListEntity>>(emptyList())
    val lists: StateFlow<List<ListEntity>> = _lists

    // =========================
    // TURNO
    // =========================
    private val _selectedShift = MutableStateFlow("Mañana")
    val selectedShift: StateFlow<String> = _selectedShift

    fun setShift(shift: String) {
        _selectedShift.value = shift

        // 🔥 recargar con la fecha actual seleccionada
        loadLists(_selectedDate.value)
    }

    // =========================
    // FECHA
    // =========================
    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate

    fun setDate(date: Long) {
        _selectedDate.value = date
        loadLists(date)
    }

    // =========================
    // TOTALES REALES
    // =========================
    private val _totalAmount = MutableStateFlow(0.0)
    val totalAmount: StateFlow<Double> = _totalAmount

    private val _totalPrize = MutableStateFlow(0.0)
    val totalPrize: StateFlow<Double> = _totalPrize

    private val _bankNet = MutableStateFlow(0.0)
    val bankNet: StateFlow<Double> = _bankNet

    private val _listeroGain = MutableStateFlow(0.0)
    val listeroGain: StateFlow<Double> = _listeroGain

    // =========================
    // CARGAR LISTAS + CALCULAR TOTALES
    // =========================
    fun loadLists(date: Long) {

        val start = getStartOfDay(date)
        val end = getEndOfDay(date)

        viewModelScope.launch {

            // 🔥 cargar listas por fecha y turno
            val loadedLists = listRepository.getListsByDateAndShift(
                start,
                end,
                _selectedShift.value
            )

            _lists.value = loadedLists

            // 🔥 calcular totales reales desde las jugadas
            var amountSum = 0.0
            var prizeSum = 0.0
            var bankSum = 0.0
            var listeroSum = 0.0

            loadedLists.forEach { list ->

                val plays = playRepository.getPlaysByList(list.id)

                plays.forEach { play ->
                    amountSum += play.amount
                    prizeSum += play.prize ?: 0.0
                    bankSum += play.bankCleanMoney - (play.prize ?: 0.0)
                    listeroSum += play.listeroCut
                }
            }

            _totalAmount.value = amountSum
            _totalPrize.value = prizeSum
            _bankNet.value = bankSum
            _listeroGain.value = listeroSum
        }
    }

    // =========================
    // FECHAS
    // =========================
    private fun getStartOfDay(date: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfDay(date: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }
}