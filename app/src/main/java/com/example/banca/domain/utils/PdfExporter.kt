package com.example.banca.domain.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object PdfExporter {

    fun exportarLista(
        context: Context,
        titulo: String,
        elementos: List<String>
    ): File {

        val document = PdfDocument()

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()

        paint.textSize = 18f
        paint.isFakeBoldText = true

        canvas.drawText(titulo, 40f, 50f, paint)

        paint.textSize = 14f
        paint.isFakeBoldText = false

        var y = 100f

        elementos.forEachIndexed { index, item ->
            canvas.drawText("${index + 1}. $item", 40f, y, paint)
            y += 30f
        }

        document.finishPage(page)

        val file = File(context.cacheDir, "$titulo.pdf")
        document.writeTo(FileOutputStream(file))
        document.close()

        return file
    }
}