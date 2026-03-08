package com.example.banca.data.repository

import com.example.banca.data.dao.PlayDao
import com.example.banca.data.toEntity
import com.example.banca.domain.models.PlayTicket

class PlayRepository(private val playDao: PlayDao) {

    // Esta función asíncrona (suspend) toma el ticket puro, lo convierte
    // a una entidad de base de datos y lo guarda en la bóveda encriptada.
    suspend fun savePlay(ticket: PlayTicket): Long {
        val entity = ticket.toEntity()
        return playDao.insertPlay(entity)
    }
}