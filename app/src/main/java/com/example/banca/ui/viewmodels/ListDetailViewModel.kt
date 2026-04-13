package com.example.banca.ui.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.banca.data.database.DatabaseProvider
import com.example.banca.data.entities.PlayEntity
import com.example.banca.data.repository.PlayRepository
import com.example.banca.domain.utils.ShiftUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.banca.data.repository.ListRepository

class ListDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val playRepository: PlayRepository
    private val listRepository: ListRepository

    private val _result = MutableStateFlow<com.example.banca.data.entities.ResultEntity?>(null)
    val result: StateFlow<com.example.banca.data.entities.ResultEntity?> = _result

    init {
        val db = DatabaseProvider.getDatabase(application)
        playRepository = PlayRepository(db.playDao())
        listRepository = ListRepository(db.listDao())
    }

    private val _plays = MutableStateFlow<List<PlayEntity>>(emptyList())
    val plays: StateFlow<List<PlayEntity>> = _plays

    fun loadPlays(listId: Long) {
        viewModelScope.launch {

            val loadedPlays = playRepository.getPlaysByList(listId)

            val pick3 = "260"
            val pick4 = "2221"
            val corridosGanadores = listOf("22", "21")

            val updatedPlays = loadedPlays.map { play ->

                val prize = when (play.playType) {
                    "FIJO" ->
                        if (play.playNumber == pick3.takeLast(2))
                            play.amount * 75
                        else 0.0

                    "CENTENA" ->
                        if (play.playNumber == pick3)
                            play.amount * 500
                        else 0.0

                    "PARLE" ->
                        if (play.playNumber == pick4)
                            play.amount * 1200
                        else 0.0

                    "CORRIDO" ->
                        if (corridosGanadores.contains(play.playNumber))
                            play.amount * 25
                        else 0.0

                    else -> 0.0
                }

                // 🔥 guardar premio real en DB
                if (prize > 0 && (play.prize ?: 0.0) == 0.0) {
                    playRepository.savePrize(play.id, prize)
                }

                play.copy(prize = prize)
            }

            _plays.value = updatedPlays
        }

    }

    fun loadResultForDate(date: Long) {
        viewModelScope.launch {
            val db = DatabaseProvider.getDatabase(getApplication())
            val resultRepo =
                com.example.banca.data.repository.ResultRepository(db.resultDao())

            _result.value = resultRepo.getResultForDate(date)
        }
    }

    fun deletePlay(playId: Long, listId: Long) {
        if (ShiftUtils.isBettingLocked()) return

        viewModelScope.launch {
            playRepository.deletePlay(playId)
            loadPlays(listId)
        }
    }

    suspend fun isCurrentListEditable(listId: Long): Boolean {

        val currentDate = System.currentTimeMillis()

        val calStart = java.util.Calendar.getInstance().apply {
            timeInMillis = currentDate
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }

        val calEnd = java.util.Calendar.getInstance().apply {
            timeInMillis = currentDate
            set(java.util.Calendar.HOUR_OF_DAY, 23)
            set(java.util.Calendar.MINUTE, 59)
            set(java.util.Calendar.SECOND, 59)
            set(java.util.Calendar.MILLISECOND, 999)
        }

        val lists = listRepository.getListsByDateAndShift(
            start = calStart.timeInMillis,
            end = calEnd.timeInMillis,
            shift = ShiftUtils.getCurrentShift()
        )

        val currentList = lists.find { it.id == listId }
            ?: return false

        return currentList.status == "OPEN"
    }
}