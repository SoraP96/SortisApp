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

    suspend fun getResultForDate(dateMillis: Long): ResultEntity? {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = dateMillis

        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)

        val startOfDay = calendar.timeInMillis

        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1)

        val endOfDay = calendar.timeInMillis

        return dao.getResultByDate(startOfDay, endOfDay)
    }
}