package com.timewise.app.domain.repository

import android.net.Uri
import com.timewise.app.domain.model.ExportResult
import com.timewise.app.domain.model.ExportsStatsReportModel

interface ExportStatsReportRepository {
    suspend fun generatePdfReport(uri: Uri, report: ExportsStatsReportModel): ExportResult

}