package com.example.banca.domain.usecases

import com.example.banca.domain.models.PlayTicket

class CalculateListTotalsUseCase {

    fun execute(plays: List<PlayTicket>): Triple<Double, Double, Double> {

        val total = plays.sumOf { it.amount }

        val listeroPayment = total * 0.20

        val bankNet = total - listeroPayment

        return Triple(total, listeroPayment, bankNet)
    }
}
