package com.example.banca.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.banca.data.entities.ResultEntity

@Dao
interface ResultDao {

    @Insert
    suspend fun insertResult(result: ResultEntity)

    @Query("SELECT * FROM results ORDER BY date DESC LIMIT 1")
    suspend fun getLatestResult(): ResultEntity?
}