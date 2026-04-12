package com.example.banca.domain.utils


import android.content.Context
import android.net.Uri
import java.io.File

object BackupManager {

    private const val DB_NAME = "banca_vault_db"

    fun exportDatabase(
        context: Context,
        destinationUri: Uri
    ) {
        val dbFile = context.getDatabasePath(DB_NAME)

        context.contentResolver.openOutputStream(destinationUri)?.use { output ->
            dbFile.inputStream().use { input ->
                input.copyTo(output)
            }
        }
    }

    fun restoreDatabase(
        context: Context,
        sourceUri: Uri
    ) {
        val dbFile = context.getDatabasePath(DB_NAME)

        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            dbFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}