package com.timewise.app.data.repository

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import com.timewise.app.domain.model.ExportResult
import com.timewise.app.domain.model.ExportsStatsReportModel
import com.timewise.app.domain.repository.ExportStatsReportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * Esta clase genera el pdf del informe.
 *
 * Constructor
 *  - @Inject constructor(@ApplicationContext private val context: Context)
 *
 *  Funciones
 *   - override suspend fun generatePdfReport(uri: Uri, report: ExportsStatsReportModel): ExportResult
 *   - private fun buildPdfDocument(report: ExportsStatsReportModel): pdfDocument
 *   - private fun drawHeader (canvas: Canvas, report: ExportsStatsReportModel)
 *   captura excepciones de E/S (IOException, SecurityException)-> ExportResult.Error
 */

class ExportStatsReportRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : ExportStatsReportRepository {
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
        val A4_WIDTH = 595
        val A4_HEIGHT = 842
        val pageInfo = PdfDocument.PageInfo.Builder(A4_WIDTH, A4_HEIGHT, 1).create()
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
        canvas.drawText("Informe semanal", 40f, 72f, subtitlePaint)
        canvas.drawLine(40f, 82f, 555f, 82f, Paint().apply {
            color = Color.parseColor("#D0D3DA")
        })
    }
    private fun drawCategoryTable (canvas: Canvas, report: ExportsStatsReportModel) {
        var y = 120f
        val rowHeight = 28f
        val headerPaint = Paint().apply {
            color = Color.parseColor("#3949AB"); textSize = 11f
            typeface = Typeface.DEFAULT_BOLD
        }
        val cellPaint = Paint().apply {
            color = Color.parseColor("#26282E"); textSize = 11f
        }
    }
    }





