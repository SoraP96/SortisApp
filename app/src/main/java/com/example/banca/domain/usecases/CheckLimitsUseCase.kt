package com.example.banca.domain.usecases

class CheckLimitsUseCase {

    private val limitsByType = mapOf(
        "FIJO" to 500.0,
        "CENTENA" to 300.0,
        "CORRIDO" to 400.0,
        "PARLE" to 200.0,
        "CANDADO" to 250.0
    )

    fun execute(
        currentTotal: Double,
        playType: String,
        newAmount: Double
    ): Boolean {

        val limit = limitsByType[
            playType.uppercase()
        ] ?: 1000.0

        return (currentTotal + newAmount) <= limit
    }
}