package com.example.banca.domain.usecases

class CheckLimitsUseCase {

    // 🚀 SIMULACIÓN: En la Fase 3, esto leerá los topes y totales reales de SQLCipher
    private val mockLimits = mapOf(
        "25" to 500.0, // El 25 tiene un tope de $500
        "33" to 300.0  // El 33 tiene un tope de $300
    )

    private val mockCurrentBets = mapOf(
        "25" to 450.0, // Ya le han apostado $450 al 25 hoy
        "33" to 0.0    // Nadie ha jugado al 33 hoy
    )

    /**
     * Devuelve 'true' si la jugada es válida (no supera el tope).
     * Devuelve 'false' si la jugada supera el tope.
     */
    fun execute(number: String, playType: String, newAmount: Double): Boolean {
        // Por ahora, solo validamos jugadas tipo "Fijo" para simplificar
        if (playType != "Fijo") return true

        val limit = mockLimits[number] ?: 1000.0 // Si no hay tope definido, le damos $1000 por defecto
        val currentlyBet = mockCurrentBets[number] ?: 0.0

        val totalAfterNewBet = currentlyBet + newAmount

        return totalAfterNewBet <= limit
    }
}