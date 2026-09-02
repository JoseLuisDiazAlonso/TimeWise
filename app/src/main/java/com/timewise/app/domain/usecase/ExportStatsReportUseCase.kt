package com.timewise.app.domain.usecase

import android.net.Uri
import com.timewise.app.domain.ads.IsFeatureUnlockedUseCase
import com.timewise.app.domain.model.CategoryExportItem
import com.timewise.app.domain.model.CategoryTimeStats
import com.timewise.app.domain.model.ExportResult
import com.timewise.app.domain.model.ExportsStatsReportModel
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.model.TimeStatsSummary
import com.timewise.app.domain.repository.ExportStatsReportRepository
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExportStatsReportUseCase @Inject constructor(
    private val getTimeStatsUseCase: GetTimeStatsUseCase,
    private val isFeatureUnlockedUseCase: IsFeatureUnlockedUseCase,
    private val exportStatsReportRepository: ExportStatsReportRepository
) {
    suspend operator fun invoke(uri: Uri): ExportResult {
        val isUnlocked = isFeatureUnlockedUseCase.execute()
        if (!isUnlocked) {
            return ExportResult.PremiumRequired
        }

        val summary: TimeStatsSummary = getTimeStatsUseCase(
            period = TimeStatsPeriod.SEMANAL,
            referenceDate = LocalDate.now()
        ).first()

        val exportModel = summary.toExportModel()

        return exportStatsReportRepository.generatePdfReport(uri, exportModel)
    }

    private fun TimeStatsSummary.toExportModel(): ExportsStatsReportModel {
        val statsList: List<CategoryTimeStats> = this.categoryStats

        val items: List<CategoryExportItem> = statsList.map { stat ->
            CategoryExportItem(
                category = stat.categoryColorHex,
                duration = Duration.ofMinutes(stat.totalMinutes),
                percentage = stat.percentage * 100f
            )
        }

        val formatter = DateTimeFormatter.ofPattern("d MMM")
        val weekPeriodLabel = "${this.periodStart.format(formatter)} – ${this.periodEnd.format(formatter)}"

        return ExportsStatsReportModel(
            weekPeriod = weekPeriodLabel,
            items = items,
            totalDuration = Duration.ofMinutes(this.totalTrackedMinutes)
        )
    }
}