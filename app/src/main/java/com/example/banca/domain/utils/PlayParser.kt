package com.example.banca.domain.utils


import com.example.banca.domain.models.PlayType

data class ParsedPlay(
    val original: String,
    val numbers: List<String>,
    val type: PlayType
)

object PlayParser {

    fun parse(number: String, type: PlayType): ParsedPlay {

        val numbers = when (type) {

            PlayType.CENTENA -> parseCentena(number)

            PlayType.FIJO -> parseFijo(number)

            PlayType.CORRIDO -> parseCorrido(number)

            PlayType.PARLE -> parseParle(number)

            PlayType.CANDADO -> parseCandado(number)
        }

        return ParsedPlay(
            original = number,
            numbers = numbers,
            type = type
        )
    }

    // -------- TIPOS --------

    private fun parseCentena(number: String): List<String> {
        return listOf(number)
    }

    private fun parseFijo(number: String): List<String> {
        return listOf(number.takeLast(2))
    }

    private fun parseCorrido(number: String): List<String> {
        if (number.length < 2) return emptyList()

        val result = mutableListOf<String>()

        for (i in 0 until number.length - 1) {
            result.add(number.substring(i, i + 2))
        }

        return result
    }

    private fun parseParle(number: String): List<String> {
        return number.split("-")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    private fun parseCandado(number: String): List<String> {
        return number.split("-")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }
}