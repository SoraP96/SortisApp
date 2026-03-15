package com.example.banca.domain.usecases

import com.example.banca.domain.models.PlayTicket

class CalculateTicketUseCase {

    fun execute(number: String, type: String, amountText: String): PlayTicket? {

        val amount = amountText.toDoubleOrNull() ?: return null

        val listeroCut = amount * 0.20
        val cleanMoney = amount - listeroCut

        return PlayTicket(
            playNumber = number,
            playType = type,
            amount = amount,
            listeroCut = listeroCut,
            bankCleanMoney = cleanMoney
        )
    }
}