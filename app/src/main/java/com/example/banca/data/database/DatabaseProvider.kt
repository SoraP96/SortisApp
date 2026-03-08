package com.example.banca.data.database

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import net.sqlcipher.database.SupportFactory
import java.security.SecureRandom
import java.util.UUID

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = buildEncryptedDatabase(context)
            INSTANCE = instance
            instance
        }
    }

    private fun buildEncryptedDatabase(context: Context): AppDatabase {
        // 1. Creamos la llave maestra en el Keystore de Android
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // 2. Usamos SharedPreferences encriptadas para guardar la contraseña de la BD
        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secret_db_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // 3. Verificamos si ya existe una contraseña. Si no, generamos una aleatoria muy segura.
        var dbPassword = sharedPreferences.getString("db_password", null)
        if (dbPassword == null) {
            dbPassword = UUID.randomUUID().toString() + SecureRandom().nextLong()
            sharedPreferences.edit().putString("db_password", dbPassword).apply()
        }

        // 4. Le entregamos la contraseña a la fábrica de SQLCipher
        val factory = SupportFactory(dbPassword.toByteArray())

        // 5. Construimos la base de datos inyectándole el motor de encriptación
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "banca_vault_db" // El nombre del archivo físico
        )
            .openHelperFactory(factory) // ¡AQUÍ SUCEDE LA ENCRIPTACIÓN!
            .fallbackToDestructiveMigration()
            .build()
    }
}