package com.example.banca.data.remote


import org.jsoup.Jsoup


data class LotteryRemoteResult(
    val date: String,
    val turno: String,
    val pick3: String?,
    val pick4: String?,
    val fireball: String?
)

class LotteryRemoteDataSource {

    companion object {
        private const val PICK3_URL =
            "https://floridalottery.com/games/draw-games/pick-3"

        private const val PICK4_URL =
            "https://floridalottery.com/games/draw-games/pick-4"
    }

    suspend fun fetchPick3(turno: String): LotteryRemoteResult {
        val document = Jsoup.connect(PICK3_URL)
            .userAgent("Mozilla/5.0")
            .timeout(15000)
            .get()

        val numbers = document
            .select(".winning-numbers .number")
            .take(3)
            .joinToString("") { it.text() }

        val fireball = document
            .select(".fireball .number")
            .firstOrNull()
            ?.text()

        return LotteryRemoteResult(
            date = java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                java.util.Locale.getDefault()
            ).format(java.util.Date()),
            turno = turno,
            pick3 = numbers,
            pick4 = null,
            fireball = fireball
        )
    }

    suspend fun fetchPick4(turno: String): LotteryRemoteResult {
        val document = Jsoup.connect(PICK4_URL)
            .userAgent("Mozilla/5.0")
            .timeout(15000)
            .get()

        val numbers = document
            .select(".winning-numbers .number")
            .take(4)
            .joinToString("") { it.text() }

        val fireball = document
            .select(".fireball .number")
            .firstOrNull()
            ?.text()

        return LotteryRemoteResult(
            date = java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                java.util.Locale.getDefault()
            ).format(java.util.Date()),
            turno = turno,
            pick3 = null,
            pick4 = numbers,
            fireball = fireball
        )
    }
}