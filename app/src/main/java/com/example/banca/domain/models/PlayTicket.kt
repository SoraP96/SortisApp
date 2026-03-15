package com.example.banca.domain.models

data class PlayTicket(

    val playNumber: String,

    val playType: String,

    val amount: Double,

    val listeroCut: Double,

    val bankCleanMoney: Double
)