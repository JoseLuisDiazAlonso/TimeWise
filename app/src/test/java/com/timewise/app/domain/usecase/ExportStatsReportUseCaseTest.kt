package com.timewise.app.domain.usecase

import android.net.Uri
import com.timewise.app.domain.ads.IsFeatureUnlockedUseCase
import com.timewise.app.domain.model.CategoryTimeStats
import com.timewise.app.domain.model.ExportResult
import com.timewise.app.domain.model.ExportsStatsReportModel
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.model.TimeStatsSummary
import com.timewise.app.domain.repository.ExportStatsReportRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDate

class ExportStatsReportUseCaseTest {

    private lateinit var getTimeStatsUseCase: GetTimeStatsUseCase
    private lateinit var isFeatureUnlockedUseCase: IsFeatureUnlockedUseCase
    private lateinit var exportStatsReportRepository: ExportStatsReportRepository
    private lateinit var useCase: ExportStatsReportUseCase
    private val uri: Uri = mockk()

    @Before
    fun setUp() {
        getTimeStatsUseCase = mockk()
        isFeatureUnlockedUseCase = mockk()
        exportStatsReportRepository = mockk()
        useCase = ExportStatsReportUseCase(getTimeStatsUseCase, isFeatureUnlockedUseCase, exportStatsReportRepository)
    }

    private fun summary(categoryStats: List<CategoryTimeStats> = emptyList(), totalMinutes: Long = 0L) =
        TimeStatsSummary(
            period = TimeStatsPeriod.SEMANAL,
            periodStart = LocalDate.of(2026, 6, 8),
            periodEnd = LocalDate.of(2026, 6, 14),
            categoryStats = categoryStats,
            totalTrackedMinutes = totalMinutes
        )

    @Test
    fun `devuelve PremiumRequired sin consultar estadisticas si la funcion esta bloqueada`() = runTest {
        coEvery { isFeatureUnlockedUseCase.execute() } returns false

        val result = useCase(uri)

        assertTrue(result is ExportResult.PremiumRequired)
        coVerify(exactly = 0) { exportStatsReportRepository.generatePdfReport(any(), any()) }
    }

    @Test
    fun `genera el pdf con los datos correctos cuando esta desbloqueada`() = runTest {
        coEvery { isFeatureUnlockedUseCase.execute() } returns true
        every {
            getTimeStatsUseCase(TimeStatsPeriod.SEMANAL, any())
        } returns flowOf(
            summary(
                categoryStats = listOf(
                    CategoryTimeStats(categoryColorHex = "#FF0000", totalMinutes = 60L, percentage = 1f)
                ),
                totalMinutes = 60L
            )
        )
        val reportSlot = slot<ExportsStatsReportModel>()
        coEvery {
            exportStatsReportRepository.generatePdfReport(uri, capture(reportSlot))
        } returns ExportResult.Success

        val result = useCase(uri)

        assertTrue(result is ExportResult.Success)
        assertEquals(1, reportSlot.captured.items.size)
        assertEquals("#FF0000", reportSlot.captured.items[0].category)
        assertEquals(Duration.ofMinutes(60L), reportSlot.captured.items[0].duration)
        assertEquals(Duration.ofMinutes(60L), reportSlot.captured.totalDuration)
    }

    @Test
    fun `propaga el error del repositorio de exportacion`() = runTest {
        coEvery { isFeatureUnlockedUseCase.execute() } returns true
        every { getTimeStatsUseCase(any(), any()) } returns flowOf(summary())
        coEvery {
            exportStatsReportRepository.generatePdfReport(any(), any())
        } returns ExportResult.Error("No se pudo escribir el archivo")

        val result = useCase(uri) as ExportResult.Error

        assertEquals("No se pudo escribir el archivo", result.message)
    }
}