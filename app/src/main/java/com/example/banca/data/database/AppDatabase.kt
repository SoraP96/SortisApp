package com.example.banca.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.banca.data.dao.PlayDao
import com.example.banca.data.dao.ListDao
import com.example.banca.data.entities.PlayEntity
import com.example.banca.data.entities.ListEntity
import com.example.banca.data.dao.ResultDao
import com.example.banca.data.entities.ResultEntity

@Database(
    entities = [
        PlayEntity::class,
        ListEntity::class,
        ResultEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playDao(): PlayDao

    abstract fun listDao(): ListDao

    abstract fun resultDao(): ResultDao
}


