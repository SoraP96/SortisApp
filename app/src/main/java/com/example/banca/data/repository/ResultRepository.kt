package com.example.banca.data.repository

import com.example.banca.data.dao.ResultDao
import com.example.banca.data.entities.ResultEntity

class ResultRepository(private val dao: ResultDao) {

    suspend fun saveResult(pick3: String, pick4: String) {

        val result = ResultEntity(
            date = System.currentTimeMillis(),
            pick3 = pick3,
            pick4 = pick4
        )

        dao.insertResult(result)
    }

    suspend fun getLatestResult(): ResultEntity? {
        return dao.getLatestResult()
    }
}