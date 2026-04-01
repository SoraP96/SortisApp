package com.example.banca.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.banca.data.entities.PlayEntity

@Dao
interface PlayDao {

    @Insert
    suspend fun insertPlay(play: PlayEntity): Long

    @Query("SELECT * FROM plays ORDER BY timestamp DESC")
    suspend fun getAllPlays(): List<PlayEntity>

    @Query("SELECT * FROM plays WHERE playNumber = :number")
    suspend fun getPlaysByNumber(number: String): List<PlayEntity>

    @Query("UPDATE plays SET prize = :prize, status = 'PAID' WHERE id = :playId")
    suspend fun updatePrize(playId: Long, prize: Double)

    @Query("SELECT * FROM plays WHERE listId = :listId")
    suspend fun getPlaysByList(listId: Long): List<PlayEntity>

}
