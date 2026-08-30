package com.timewise.app.domain.model

import java.time.Duration

/**
 * Esta data class representa el modelo de los datos ya agregados que se van a exportar
 */

data class ExportsStatsReportModel(
    val weekPeriod: String,
    val items: List<CategoryExportItem>,
    val totalDuration: Duration
)
