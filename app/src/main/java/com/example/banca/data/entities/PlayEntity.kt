package com.example.banca.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity le dice a Room que esto será una tabla en SQLCipher
@Entity(tableName = "plays")
data class PlayEntity(
    // Clave primaria autoincremental (el ID del ticket)
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val playNumber: String,
    val playType: String,
    val totalAmount: Double,
    val listeroCut: Double,
    val bankCleanMoney: Double,

    // Guardamos la marca de tiempo exacta de cuándo se hizo la jugada
    val timestamp: Long = System.currentTimeMillis()
)