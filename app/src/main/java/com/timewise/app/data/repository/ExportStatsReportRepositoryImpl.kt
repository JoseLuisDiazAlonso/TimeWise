package com.timewise.app.data.repository

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.timewise.app.domain.model.CategoryExportItem
import com.timewise.app.domain.model.ExportResult
import com.timewise.app.domain.model.ExportsStatsReportModel
import com.timewise.app.domain.repository.ExportStatsReportRepository
import com.timewise.app.ui.timeblocking.categoryOptionForHex
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import javax.inject.Inject

/**
 * Esta clase genera el pdf del informe.
 *
 * Constructor
 *  - @Inject constructor(@ApplicationContext private val context: Context)
 *
 *  Funciones
 *   - override suspend fun generatePdfReport(uri: Uri, report: ExportsStatsReportModel): ExportResult
 *   - private fun buildPdfDocument(report: ExportsStatsReportModel): PdfDocument
 *   - private fun drawHeader(canvas: Canvas, report: ExportsStatsReportModel)
 *   - private fun drawCategoryTable(canvas: Canvas, report: ExportsStatsReportModel)
 *   captura excepciones de E/S (IOException, SecurityException) -> ExportResult.Error
 *
 *   Nota: item.category en CategoryExportItem contiene el colorHex crudo (ver
 *   ExportStatsReportUseCase.toExportModel), no un nombre legible. Se resuelve aquí
 *   con categoryOptionForHex(), el mismo helper que ya usa StatisticsScreen para lo mismo.
 */

class ExportStatsReportRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ExportStatsReportRepository {

    override suspend fun generatePdfReport(uri: Uri, report: ExportsStatsReportModel): ExportResult {
        return try {
            val pdfDocument = buildPdfDocument(report)
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                pdfDocument.writeTo(outputStream)
            }
            pdfDocument.close()
            ExportResult.Success
        } catch (e: Exception) {
            ExportResult.Error(e.message ?: "Unknown error")
        }
    }

    private fun buildPdfDocument(report: ExportsStatsReportModel): PdfDocument {
        val pdfDocument = PdfDocument()
        val a4Width = 595
        val a4Height = 842
        val pageInfo = PdfDocument.PageInfo.Builder(a4Width, a4Height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        drawHeader(canvas, report)
        drawCategoryTable(canvas, report)

        pdfDocument.finishPage(page)
        return pdfDocument
    }

    private fun drawHeader(canvas: Canvas, report: ExportsStatsReportModel) {
        val titlePaint = Paint().apply {
            color = Color.parseColor("#3949AB")
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
        }
        val subtitlePaint = Paint().apply {
            color = Color.parseColor("#4A4C55")
            textSize = 12f
        }
        canvas.drawText("TimeWise", 40f, 50f, titlePaint)
        canvas.drawText("Informe semanal · ${report.weekPeriod}", 40f, 72f, subtitlePaint)
        canvas.drawLine(40f, 82f, 555f, 82f, Paint().apply {
            color = Color.parseColor("#D0D3DA")
        })
    }

    private fun drawCategoryTable(canvas: Canvas, report: ExportsStatsReportModel) {
        var y = 120f
        val rowHeight = 28f

        val headerPaint = Paint().apply {
            color = Color.parseColor("#3949AB")
            textSize = 11f
            typeface = Typeface.DEFAULT_BOLD
        }
        val cellPaint = Paint().apply {
            color = Color.parseColor("#26282E")
            textSize = 11f
        }
        val linePaint = Paint().apply {
            color = Color.parseColor("#E4E6EB")
        }

        // Cabecera de columnas
        canvas.drawText("Categoría", 40f, y, headerPaint)
        canvas.drawText("Tiempo", 300f, y, headerPaint)
        canvas.drawText("% del total", 440f, y, headerPaint)
        y += 12f
        canvas.drawLine(40f, y, 555f, y, linePaint)
        y += rowHeight

        // Filas de datos, una por categoría
        report.items.forEach { item: CategoryExportItem ->
            val categoryName = context.getString(categoryOptionForHex(item.category).labelRes)
            canvas.drawText(categoryName, 40f, y, cellPaint)
            canvas.drawText(formatDuration(item.duration), 300f, y, cellPaint)
            canvas.drawText("${item.percentage.toInt()}%", 440f, y, cellPaint)
            y += rowHeight
            canvas.drawLine(40f, y - rowHeight + 10f, 555f, y - rowHeight + 10f, linePaint)
        }

        // Fila de total
        y += 8f
        canvas.drawLine(40f, y - rowHeight + 10f, 555f, y - rowHeight + 10f, headerPaint)
        canvas.drawText("Total", 40f, y, headerPaint)
        canvas.drawText(formatDuration(report.totalDuration), 300f, y, headerPaint)
    }

    private fun formatDuration(duration: Duration): String {
        val hours = duration.toHours()
        val minutes = duration.toMinutesPart()
        return if (hours > 0) "${hours}h ${minutes}min" else "${minutes}min"
    }
}