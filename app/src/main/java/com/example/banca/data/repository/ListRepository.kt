package com.example.banca.data.repository


import com.example.banca.data.dao.ListDao
import com.example.banca.data.entities.ListEntity

class ListRepository(private val listDao: ListDao) {

    suspend fun createList(listeroCode: String): Long {

        val list = ListEntity(
            listeroCode = listeroCode
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

}
