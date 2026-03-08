package com.example.banca.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.banca.data.dao.PlayDao
import com.example.banca.data.entities.PlayEntity

@Database(entities = [PlayEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // Conectamos el DAO a la base de datos
    abstract fun playDao(): PlayDao
}