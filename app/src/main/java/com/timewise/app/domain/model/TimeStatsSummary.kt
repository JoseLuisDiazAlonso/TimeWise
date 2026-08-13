package com.timewise.app.domain.model

import java.time.LocalDate

/**
 * Esta clase es el resultado completo que consume la UI: el periodo consultado, su rango de
 * fechas resuelto y la lista de estadísticas por categoría.
 * **/

data class TimeStatsSummary (
    val period: TimeStatsPeriod,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val categoryStats: List<CategoryTimeStats>,
    val totalTrackedMinutes: Long
)