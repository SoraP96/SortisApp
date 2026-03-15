package com.example.banca.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val listeroCode: String,

    val totalAmount: Double = 0.0,

    val listeroPayment: Double = 0.0,

    val bankNet: Double = 0.0,

    val status: String = "OPEN",

    val createdAt: Long = System.currentTimeMillis()
)
