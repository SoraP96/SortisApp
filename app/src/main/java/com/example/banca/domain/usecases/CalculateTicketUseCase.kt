package com.example.banca.domain.usecases

import com.example.banca.domain.models.PlayTicket
import com.example.banca.domain.models.PlayType

class CalculateTicketUseCase {

    fun execute(number: String, type: String, amountText: String): PlayTicket? {

        val amount = amountText.toDoubleOrNull() ?: return null

        val listeroCut = amount * 0.20
        val cleanMoney = amount - listeroCut

        return PlayTicket(
            playNumber = number,
            playType =  mapToPlayType(type) ?: return null,
            amount = amount,
            listeroCut = listeroCut,
            bankCleanMoney = cleanMoney
        )
    }

    private fun mapToPlayType(type: String): PlayType? {
        return when (type.lowercase()) {
            "fijo" -> PlayType.FIJO
            "centena" -> PlayType.CENTENA
            "parle" -> PlayType.PARLE
            "corrido" -> PlayType.CORRIDO
            "candado" -> PlayType.CANDADO
            else -> null
        }
    }
}