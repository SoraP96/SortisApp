package com.example.banca.ui.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.banca.data.database.DatabaseProvider
import com.example.banca.data.remote.LotteryRemoteDataSource
import com.example.banca.data.repository.LotteryResultsRepository
import com.example.banca.data.repository.PlayRepository
import com.example.banca.data.repository.ResultRepository
import com.example.banca.domain.usecases.VerifyPrizesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val resultRepository: ResultRepository
    private val playRepository: PlayRepository

    private val verifyPrizesUseCase: VerifyPrizesUseCase

    init {
        val db = DatabaseProvider.getDatabase(application)

        val resultDao = db.resultDao()
        val playDao = db.playDao()

        resultRepository = ResultRepository(resultDao)
        playRepository = PlayRepository(playDao)

        verifyPrizesUseCase = VerifyPrizesUseCase(playRepository, resultRepository)

        viewModelScope.launch {
            try {
                val syncRepository = LotteryResultsRepository(
                    dao = db.resultDao(),
                    remoteDataSource = LotteryRemoteDataSource()
                )

                syncRepository.syncLatestResults("pick3")
                syncRepository.syncLatestResults("pick4")

                val latest = resultRepository.getLatestResult()
                latest?.let {
                    _pick3.value = it.pick3
                    _pick4.value = it.pick4
                }

            } catch (_: Exception) {
            }
        }
    }

    private val _pick3 = MutableStateFlow("260")
    val pick3: StateFlow<String> = _pick3.asStateFlow()

    private val _pick4 = MutableStateFlow("2221")
    val pick4: StateFlow<String> = _pick4.asStateFlow()

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status.asStateFlow()

    fun onPick3Change(value: String) {
        _pick3.value = value
    }

    fun onPick4Change(value: String) {
        _pick4.value = value
    }

    fun saveResult() {
        viewModelScope.launch {
            resultRepository.saveResult(_pick3.value, _pick4.value)
            _status.value = "Resultado guardado"
        }
    }

    fun calculatePrizes() {
        viewModelScope.launch {
            verifyPrizesUseCase.execute()
            _status.value = "Premios calculados"
        }
    }

    private val _plays = MutableStateFlow<List<com.example.banca.data.entities.PlayEntity>>(emptyList())
    val plays: StateFlow<List<com.example.banca.data.entities.PlayEntity>> = _plays

    fun loadResults() {
        viewModelScope.launch {
            _plays.value = playRepository.getAllPlays()
        }
    }
}



