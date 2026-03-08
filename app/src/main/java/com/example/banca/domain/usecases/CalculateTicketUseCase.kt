package com.example.banca.domain.usecases

import com.example.banca.domain.models.PlayTicket

class CalculateTicketUseCase {

    fun execute(number: String, type: String, amountText: String): PlayTicket? {
        // 1. Convertimos el texto del teclado a dinero real
        val amount = amountText.toDoubleOrNull() ?: return null

        // 2. Reglas de negocio del banco
        val listeroPercentage = 0.20 // 20%
        val listeroCut = amount * listeroPercentage
        val cleanMoney = amount - listeroCut

        // 3. Empaquetamos el resultado en nuestro ticket
        return PlayTicket(
            playNumber = number,
            playType = type,
            totalAmount = amount,
            listeroCut = listeroCut,
            bankCleanMoney = cleanMoney
        )
    }
}