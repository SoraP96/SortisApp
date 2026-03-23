package com.example.banca.domain.usecases


import com.example.banca.data.entities.PlayEntity
import com.example.banca.data.repository.PlayRepository
import com.example.banca.data.repository.ResultRepository
import com.example.banca.domain.models.PlayType
import com.example.banca.domain.utils.PlayParser

class VerifyPrizesUseCase(
    private val playRepository: PlayRepository,
    private val resultRepository: ResultRepository
) {

    suspend fun execute() {

        val result = resultRepository.getLatestResult() ?: return

        val plays = playRepository.getAllPlays()

        for (play in plays) {

            val playType = PlayType.valueOf(play.playType)

            val parsed = PlayParser.parse(play.playNumber, playType)

            val prize = calculatePrize(parsed, play, result)

            if (prize > 0) {
                playRepository.updatePrize(play.id, prize)
            }
        }
    }

    private fun calculatePrize(
        parsed: com.example.banca.domain.utils.ParsedPlay,
        play: PlayEntity,
        result: com.example.banca.data.entities.ResultEntity
    ): Double {

        val pick3 = result.pick3
        val pick4 = result.pick4

        return when (parsed.type) {

            PlayType.CENTENA -> {
                if (parsed.numbers.contains(pick3)) {
                    play.amount * 500
                } else 0.0
            }

            PlayType.FIJO -> {
                val last2 = pick3.takeLast(2)
                if (parsed.numbers.contains(last2)) {
                    play.amount * 75
                } else 0.0
            }

            PlayType.CORRIDO -> {
                val corridos = PlayParser.parse(pick4, PlayType.CORRIDO).numbers
                val hits = parsed.numbers.count { corridos.contains(it) }
                play.amount * 25 * hits
            }

            PlayType.PARLE -> {
                val corridos = PlayParser.parse(pick4, PlayType.CORRIDO).numbers
                if (parsed.numbers.all { corridos.contains(it) }) {
                    play.amount * 1200
                } else 0.0
            }

            PlayType.CANDADO -> {
                val corridos = PlayParser.parse(pick4, PlayType.CORRIDO).numbers
                val hits = parsed.numbers.count { corridos.contains(it) }

                when (hits) {
                    3 -> play.amount * 2000 // puedes ajustar
                    2 -> play.amount * 200
                    else -> 0.0
                }
            }
        }
    }
}