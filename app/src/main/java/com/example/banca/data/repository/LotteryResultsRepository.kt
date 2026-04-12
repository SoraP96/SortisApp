package com.example.banca.data.repository

import com.example.banca.data.dao.ResultDao
import com.example.banca.data.entities.ResultEntity
import com.example.banca.data.remote.LotteryRemoteDataSource

class LotteryResultsRepository(
    private val dao: ResultDao,
    private val remoteDataSource: LotteryRemoteDataSource
) {

    suspend fun syncLatestResults(turno: String) {
        val remote = if (turno.lowercase() == "pick3") {
            remoteDataSource.fetchPick3(turno)
        } else {
            remoteDataSource.fetchPick4(turno)
        }

        dao.insertResult(
            ResultEntity(
                date = System.currentTimeMillis(),
                pick3 = remote.pick3 ?: "",
                pick4 = remote.pick4 ?: ""
            )
        )
    }
}