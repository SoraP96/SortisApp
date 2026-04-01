package com.example.banca.ui.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.banca.data.database.DatabaseProvider
import com.example.banca.data.entities.PlayEntity
import com.example.banca.data.repository.PlayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val playRepository: PlayRepository

    init {
        val db = DatabaseProvider.getDatabase(application)
        playRepository = PlayRepository(db.playDao())
    }

    private val _plays = MutableStateFlow<List<PlayEntity>>(emptyList())
    val plays: StateFlow<List<PlayEntity>> = _plays

    fun loadPlays(listId: Long) {
        viewModelScope.launch {

            val loadedPlays = playRepository.getPlaysByList(listId)

            val pick3 = "260"
            val pick4 = "2221"

            val updatedPlays = loadedPlays.map { play ->

                val prize = when {

                    play.playType == "FIJO" &&
                            play.playNumber == pick3.takeLast(2) -> play.amount * 75

                    play.playType == "PARLE" &&
                            play.playNumber == pick4 -> play.amount * 1200

                    play.playType == "CORRIDO" &&
                            pick3.contains(play.playNumber) -> play.amount * 25

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
}