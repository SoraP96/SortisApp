package com.example.banca.data.repository


import com.example.banca.data.dao.ListDao
import com.example.banca.data.entities.ListEntity

class ListRepository(private val listDao: ListDao) {

    suspend fun createList(listeroCode: String, shift: String): Long {

        val list = ListEntity(
            listeroCode = listeroCode,
            date = System.currentTimeMillis(),
            shift = shift
        )

        return listDao.insertList(list)
    }

    suspend fun getOpenList(): ListEntity? {
        return listDao.getOpenList()
    }

    suspend fun updateTotals(
        listId: Long,
        total: Double,
        listero: Double,
        bank: Double
    ) {
        listDao.updateTotals(listId, total, listero, bank)
    }

    suspend fun closeList(listId: Long) {
        listDao.closeList(listId)
    }

    suspend fun getListsByDateAndShift(
        start: Long,
        end: Long,
        shift: String
    ) = listDao.getListsByDateAndShift(start, end, shift)

}
