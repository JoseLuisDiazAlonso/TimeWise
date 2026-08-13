package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.CategoryTimeStats
import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.model.TimeStatsSummary
import com.timewise.app.domain.repository.TimeBlockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

/**
 * El objetivo de esta clase es dado un periodo (SEMANAL/MENSUAL) y un fecha de referencia,
 * calcular el rango de fechas correspondientes, pide los TimeBlock de ese rango al repositorio y
 * los agrupa por categoría para devolver el resultado.
 * **/

class GetTimeStatsUseCase @Inject constructor(
    private val timeBlockRepository: TimeBlockRepository
) {
    operator fun invoke(
        period: TimeStatsPeriod,
        referenceDate: LocalDate
    ): Flow<TimeStatsSummary> {
        val (start, end) = resolveRange(period, referenceDate)
        return timeBlockRepository.observeTimeBlocksForDate(start).map{ blocks -> aggregate(period, start, end, blocks) }
    }
    private fun resolveRange(
        period: TimeStatsPeriod,
        referenceDate: LocalDate
    ): Pair<LocalDate, LocalDate> = when (period) {
        TimeStatsPeriod.SEMANAL -> {
            val start = referenceDate.with(java.time.DayOfWeek.MONDAY)
            start to start.plusDays(6)
        }
        TimeStatsPeriod.MENSUAL -> {
            val start = referenceDate.withDayOfMonth(1)
            start to referenceDate.withDayOfMonth(referenceDate.lengthOfMonth())
        }
    }
    private fun aggregate(
        period: TimeStatsPeriod,
        start: LocalDate,
        end: LocalDate,
        blocks: List<TimeBlock>
    ): TimeStatsSummary {
        val totalMinutes = blocks.sumOf { minutesOf(it) }
        val categoryStats = blocks
            .groupBy { it.colorHex }
            .map { (colorHex, blocksInColor) ->
                val minutes = blocksInColor.sumOf { minutesOf(it) }
                CategoryTimeStats(
                    categoryColorHex = colorHex,
                    totalMinutes = minutes,
                    percentage = if (totalMinutes == 0L) 0f
                    else minutes.toFloat() / totalMinutes.toFloat()
                )
            }
            .sortedByDescending { it.totalMinutes }
        return TimeStatsSummary(
            period = period,
            periodStart = start,
            periodEnd = end,
            categoryStats = categoryStats,
            totalTrackedMinutes = totalMinutes
        )
    }
    private fun minutesOf(block: TimeBlock): Long =
        (block.endTime - block.startTime) / 60_000L
}