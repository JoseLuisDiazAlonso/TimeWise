package com.timewise.app.ui.statistics

import com.timewise.app.domain.model.CategoryTimeStats
import com.timewise.app.domain.model.TimeStatsPeriod
import java.time.LocalDate

data class StatisticsUiState (
    val period: TimeStatsPeriod,
    val referenceDate: LocalDate,
    val categoryStats: List<CategoryTimeStats>,
    val totalMinutes: Long,
    val isPremiumUnlocked: Boolean,
    val isLoading: Boolean
)