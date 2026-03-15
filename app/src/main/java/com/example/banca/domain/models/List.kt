package com.example.banca.domain.models


data class List(

    val id: Long,

    val listeroCode: String,

    val totalAmount: Double,

    val listeroPayment: Double,

    val bankNet: Double,

    val status: String
)
