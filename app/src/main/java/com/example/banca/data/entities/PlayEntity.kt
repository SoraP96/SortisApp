package com.example.banca.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plays")
data class PlayEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val listId: Long? = null,

    val playNumber: String,

    val playType: String,

    val amount: Double,

    val listeroCut: Double,

    val bankCleanMoney: Double,

    val prize: Double? = null,

    val status: String = "ACTIVE",

    val timestamp: Long = System.currentTimeMillis()
)

