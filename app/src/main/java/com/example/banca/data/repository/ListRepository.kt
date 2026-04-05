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

    fun isListEditable(list: ListEntity): Boolean {

        val today = System.currentTimeMillis()

        val startOfDay = getStartOfDay(today)
        val endOfDay = getEndOfDay(today)

        val isToday = list.date in startOfDay..endOfDay

        return list.status == "OPEN" && isToday
    }

    private fun getStartOfDay(date: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfDay(date: Long): Long {
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(java.util.Calendar.HOUR_OF_DAY, 23)
        cal.set(java.util.Calendar.MINUTE, 59)
        cal.set(java.util.Calendar.SECOND, 59)
        cal.set(java.util.Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

}
