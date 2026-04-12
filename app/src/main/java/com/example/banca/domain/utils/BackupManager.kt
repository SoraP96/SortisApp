package com.example.banca.domain.utils

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.example.banca.data.database.DatabaseProvider
import org.json.JSONObject

object BackupManager {

    private const val DB_NAME = "banca_vault_db"

    fun exportPortableBackup(
        context: Context,
        destinationUri: Uri
    ) {
        val dbFile = context.getDatabasePath(DB_NAME)
        val dbBytes = dbFile.readBytes()

        val password =
            DatabaseProvider.getDatabasePassword(context)

        val backupJson = JSONObject().apply {
            put("db_password", password)
            put(
                "db_bytes",
                Base64.encodeToString(
                    dbBytes,
                    Base64.DEFAULT
                )
            )
        }

        context.contentResolver
            .openOutputStream(destinationUri)
            ?.bufferedWriter()
            ?.use {
                it.write(backupJson.toString())
            }
    }

    fun restorePortableBackup(
        context: Context,
        sourceUri: Uri
    ) {
        val jsonText = context.contentResolver
            .openInputStream(sourceUri)
            ?.bufferedReader()
            ?.readText()
            ?: return

        val json = JSONObject(jsonText)

        val password = json.getString("db_password")
        val dbBytes = Base64.decode(
            json.getString("db_bytes"),
            Base64.DEFAULT
        )

        DatabaseProvider.setDatabasePassword(
            context,
            password
        )

        val dbFile = context.getDatabasePath(DB_NAME)
        dbFile.writeBytes(dbBytes)

        DatabaseProvider.resetDatabaseInstance()
    }
}