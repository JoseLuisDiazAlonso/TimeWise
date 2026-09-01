package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.repository.TimeBlockRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class GetTimeStatsUseCaseTest {

    private lateinit var timeBlockRepository: TimeBlockRepository
    private lateinit var useCase: GetTimeStatsUseCase

    @Before
    fun setUp() {
        timeBlockRepository = mockk()
        useCase = GetTimeStatsUseCase(timeBlockRepository)
    }

    private fun timeBlock(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        colorHex: String
    ) = TimeBlock(
        title = "Bloque de prueba",
        date = date,
        startTime = startTime,
        endTime = endTime,
        colorHex = colorHex
    )

    @Test
    fun `SEMANAL calcula el rango de lunes a domingo`() = runTest {
        val startSlot = slot<LocalDate>()
        val endSlot = slot<LocalDate>()
        every {
            timeBlockRepository.observeByDateRange(capture(startSlot), capture(endSlot))
        } returns flowOf(emptyList())

        // Miércoles 2026-06-10 -> semana del lunes 08 al domingo 14
        useCase(TimeStatsPeriod.SEMANAL, LocalDate.of(2026, 6, 10)).first()

        assertEquals(LocalDate.of(2026, 6, 8), startSlot.captured)
        assertEquals(LocalDate.of(2026, 6, 14), endSlot.captured)
    }

    @Test
    fun `MENSUAL calcula el rango del primer al ultimo dia del mes`() = runTest {
        val startSlot = slot<LocalDate>()
        val endSlot = slot<LocalDate>()
        every {
            timeBlockRepository.observeByDateRange(capture(startSlot), capture(endSlot))
        } returns flowOf(emptyList())

        useCase(TimeStatsPeriod.MENSUAL, LocalDate.of(2026, 2, 15)).first()

        assertEquals(LocalDate.of(2026, 2, 1), startSlot.captured)
        assertEquals(LocalDate.of(2026, 2, 28), endSlot.captured)
    }

    @Test
    fun `agrupa por categoria y suma los minutos correctamente`() = runTest {
        val monday = LocalDate.of(2026, 6, 8)
        val blocks = listOf(
            timeBlock(monday, LocalTime.of(9, 0), LocalTime.of(10, 0), "#FF0000"), // 60 min
            timeBlock(monday, LocalTime.of(10, 0), LocalTime.of(10, 30), "#FF0000"), // 30 min
            timeBlock(monday.plusDays(1), LocalTime.of(9, 0), LocalTime.of(9, 30), "#00FF00") // 30 min
        )
        every { timeBlockRepository.observeByDateRange(any(), any()) } returns flowOf(blocks)

        val result = useCase(TimeStatsPeriod.SEMANAL, LocalDate.of(2026, 6, 10)).first()

        assertEquals(120L, result.totalTrackedMinutes)
        assertEquals(2, result.categoryStats.size)
        assertEquals("#FF0000", result.categoryStats[0].categoryColorHex)
        assertEquals(90L, result.categoryStats[0].totalMinutes)
        assertEquals(0.75f, result.categoryStats[0].percentage, 0.0001f)
        assertEquals("#00FF00", result.categoryStats[1].categoryColorHex)
        assertEquals(30L, result.categoryStats[1].totalMinutes)
        assertEquals(0.25f, result.categoryStats[1].percentage, 0.0001f)
    }

    @Test
    fun `lista vacia devuelve totales en cero sin dividir por cero`() = runTest {
        every { timeBlockRepository.observeByDateRange(any(), any()) } returns flowOf(emptyList())

        val result = useCase(TimeStatsPeriod.SEMANAL, LocalDate.of(2026, 6, 10)).first()

        assertEquals(0L, result.totalTrackedMinutes)
        assertEquals(emptyList<Any>(), result.categoryStats)
    }

    @Test
    fun `expone el periodo y las fechas en el resultado`() = runTest {
        every { timeBlockRepository.observeByDateRange(any(), any()) } returns flowOf(emptyList())

        val result = useCase(TimeStatsPeriod.MENSUAL, LocalDate.of(2026, 4, 5)).first()

        assertEquals(TimeStatsPeriod.MENSUAL, result.period)
        assertEquals(LocalDate.of(2026, 4, 1), result.periodStart)
        assertEquals(LocalDate.of(2026, 4, 30), result.periodEnd)
    }
}