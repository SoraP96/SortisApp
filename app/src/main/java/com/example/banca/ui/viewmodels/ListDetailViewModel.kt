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
            _plays.value = playRepository.getPlaysByList(listId)
        }
    }
}