package com.example.banca.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.banca.data.entities.ResultEntity
import androidx.room.OnConflictStrategy

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: ResultEntity)

    @Query("SELECT * FROM results ORDER BY date DESC LIMIT 1")
    suspend fun getLatestResult(): ResultEntity?

    @Query("""
    SELECT * FROM results
    WHERE date >= :startDate
    AND date < :endDate
    ORDER BY date DESC
    LIMIT 1
""")
    suspend fun getResultByDate(
        startDate: Long,
        endDate: Long
    ): ResultEntity?
}