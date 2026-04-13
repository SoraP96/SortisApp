package com.example.banca.domain.utils

import java.util.Calendar
import java.util.TimeZone

object ShiftUtils {

    private val easternTimeZone =
        TimeZone.getTimeZone("America/New_York")

    private fun getEasternTotalMinutes(): Int {
        val cal = Calendar.getInstance(easternTimeZone)

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return hour * 60 + minute
    }

    fun getCurrentShift(): String {
        val totalMinutes = getEasternTotalMinutes()

        return if (totalMinutes < (13 * 60 + 30)) {
            "Mañana"
        } else {
            "Noche"
        }
    }

    fun isBettingLocked(): Boolean {
        val totalMinutes = getEasternTotalMinutes()

        val dayLockStart = 13 * 60 + 15
        val dayDrawTime = 13 * 60 + 30

        val nightLockStart = 21 * 60 + 30
        val nightDrawTime = 21 * 60 + 45

        val dayLocked =
            totalMinutes in dayLockStart until dayDrawTime

        val nightLocked =
            totalMinutes in nightLockStart until nightDrawTime

        return dayLocked || nightLocked
    }

    fun shouldFetchLotteryResults(): Boolean {
        val totalMinutes = getEasternTotalMinutes()

        val dayFetchStart = 13 * 60 + 30
        val dayFetchEnd = 13 * 60 + 45

        val nightFetchStart = 21 * 60 + 45
        val nightFetchEnd = 22 * 60

        val dayFetch =
            totalMinutes in dayFetchStart until dayFetchEnd

        val nightFetch =
            totalMinutes in nightFetchStart until nightFetchEnd

        return dayFetch || nightFetch
    }
}