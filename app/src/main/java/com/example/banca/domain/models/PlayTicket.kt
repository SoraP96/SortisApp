package com.example.banca.domain.models

data class PlayTicket(
    val playNumber: String,
    val playType: String, // "Fijo", "Corrido", "Parle"
    val totalAmount: Double,
    val listeroCut: Double, // El 20% para el listero
    val bankCleanMoney: Double // El 80% para el banco
)