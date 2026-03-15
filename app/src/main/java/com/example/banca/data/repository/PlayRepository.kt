package com.example.banca.data.repository

import com.example.banca.data.dao.PlayDao
import com.example.banca.data.toEntity
import com.example.banca.domain.models.PlayTicket
import com.example.banca.data.entities.PlayEntity

class PlayRepository(private val playDao: PlayDao) {

    suspend fun savePlay(ticket: PlayTicket, listId: Long): Long {
        val entity = ticket.toEntity(listId)
        return playDao.insertPlay(entity)
    }

    suspend fun getAllPlays() = playDao.getAllPlays()

    suspend fun getPlaysByNumber(number: String) =
        playDao.getPlaysByNumber(number)

    suspend fun updatePrize(playId: Long, prize: Double) =
        playDao.updatePrize(playId, prize)
}
