package com.example.banca.domain.utils


import java.util.Calendar

object ShiftUtils {

    fun getCurrentShift(): String {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val totalMinutes = hour * 60 + minute

        return if (totalMinutes < (13 * 60 + 30)) {
            "DAY"
        } else {
            "NIGHT"
        }
    }

    fun isBettingLocked(): Boolean {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val totalMinutes = hour * 60 + minute

        val dayLock = 13 * 60 + 15
        val nightLock = 21 * 60 + 30

        return totalMinutes in dayLock until (13 * 60 + 30) ||
                totalMinutes in nightLock until (21 * 60 + 45)
    }
}