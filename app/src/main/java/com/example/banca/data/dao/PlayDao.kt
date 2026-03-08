package com.example.banca.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.banca.data.entities.PlayEntity

@Dao
interface PlayDao {
    // Esta función insertará el ticket en la tabla encriptada
    // Le añadimos ": Long" para que devuelva el ID del ticket insertado
    // ¡Esto elimina el error de la firma "V" (Void) en KSP!
    @Insert
    suspend fun insertPlay(play: PlayEntity): Long
}