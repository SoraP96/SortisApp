package com.example.banca.data

import com.example.banca.data.entities.PlayEntity
import com.example.banca.domain.models.PlayTicket

// Esta función transforma nuestro ticket puro en una fila para la base de datos
fun PlayTicket.toEntity(listId:Long): PlayEntity {
    return PlayEntity(
        listId = listId,
        playNumber = this.playNumber,
        playType = this.playType,
        amount = this.amount,
        listeroCut = this.listeroCut,
        bankCleanMoney = this.bankCleanMoney
    )
}
