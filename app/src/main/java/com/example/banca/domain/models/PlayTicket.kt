package com.example.banca.domain.models
import com.example.banca.domain.models.PlayType

data class PlayTicket(

    val playNumber: String,

    val playType: PlayType,

    val amount: Double,

    val listeroCut: Double,

    val bankCleanMoney: Double
)