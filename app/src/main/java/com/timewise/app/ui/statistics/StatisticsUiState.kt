package com.timewise.app.ui.statistics

import com.timewise.app.domain.model.CategoryTimeStats
import com.timewise.app.domain.model.TimeStatsPeriod
import java.time.LocalDate

data class StatisticsUiState(
    val period: TimeStatsPeriod = TimeStatsPeriod.SEMANAL,
    val periodStart: LocalDate = LocalDate.now(),
    val periodEnd: LocalDate = LocalDate.now(),
    val categoryStats: List<CategoryTimeStats> = emptyList(),
    val totalTrackedMinutes: Long = 0,
    val isLoading: Boolean = true,
    val isUnlocked: Boolean = false
) {
    val isEmpty: Boolean get() = totalTrackedMinutes == 0L
}