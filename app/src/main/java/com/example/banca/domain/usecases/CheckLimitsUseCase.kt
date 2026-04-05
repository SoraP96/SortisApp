package com.example.banca.domain.usecases

class CheckLimitsUseCase {

    private val mockLimits = mapOf(
        "FIJO" to 500.0,
        "CENTENA" to 300.0,
        "CORRIDO" to 400.0,
        "PARLE" to 200.0,
        "CANDADO" to 250.0
    )

    private val mockCurrentBets = mutableMapOf(
        "25_FIJO" to 450.0,
        "260_CENTENA" to 100.0,
        "22_CORRIDO" to 50.0,
        "2221_PARLE" to 0.0
    )

    fun execute(
        number: String,
        playType: String,
        newAmount: Double
    ): Boolean {

        val typeKey = playType.uppercase()

        val limit = mockLimits[typeKey] ?: 1000.0

        val currentKey = "${number}_$typeKey"
        val currentlyBet = mockCurrentBets[currentKey] ?: 0.0

        val totalAfterNewBet = currentlyBet + newAmount

        return totalAfterNewBet <= limit
    }
}