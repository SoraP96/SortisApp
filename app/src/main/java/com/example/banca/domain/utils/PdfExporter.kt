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
        elementos: List<String>,
        totalJugado: Double,
        premios: Double,
        banco: Double,
        listero: Double
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

        var y = 90f

        canvas.drawText("Resultados Lotto", 40f, y, paint)
        y += 30f

        canvas.drawText("Pick3: 260", 40f, y, paint)
        y += 25f
        canvas.drawText("Pick4: 2221", 40f, y, paint)
        y += 35f

        canvas.drawText("Total jugado: ${"%.0f".format(totalJugado)}", 40f, y, paint)
        y += 25f
        canvas.drawText("Premios: ${"%.0f".format(premios)}", 40f, y, paint)
        y += 25f
        canvas.drawText("Banco: ${"%.0f".format(banco)}", 40f, y, paint)
        y += 25f
        canvas.drawText("Listero: ${"%.0f".format(listero)}", 40f, y, paint)
        y += 40f

        canvas.drawText("Jugadas:", 40f, y, paint)
        y += 30f

        elementos.forEachIndexed { index, item ->
            canvas.drawText("${index + 1}. $item", 40f, y, paint)
            y += 25f
        }

        document.finishPage(page)

        val file = File(
            context.getExternalFilesDir(null),
            "$titulo.pdf"
        )

        document.writeTo(FileOutputStream(file))
        document.close()

        return file
    }
}