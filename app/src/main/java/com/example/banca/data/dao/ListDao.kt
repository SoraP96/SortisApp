package com.example.banca.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.banca.data.entities.ListEntity

@Dao
interface ListDao {

    @Insert
    suspend fun insertList(list: ListEntity): Long

    @Query("SELECT * FROM lists WHERE status = 'OPEN' LIMIT 1")
    suspend fun getOpenList(): ListEntity?

    @Query("UPDATE lists SET totalAmount = :total, listeroPayment = :listero, bankNet = :bank WHERE id = :listId")
    suspend fun updateTotals(
        listId: Long,
        total: Double,
        listero: Double,
        bank: Double
    )

    @Query("UPDATE lists SET status = 'CLOSED' WHERE id = :listId")
    suspend fun closeList(listId: Long)

}
