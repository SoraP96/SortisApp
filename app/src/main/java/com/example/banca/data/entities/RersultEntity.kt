package com.example.banca.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results")
data class ResultEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val date: Long,

    val pick3: String,

    val pick4: String
)

