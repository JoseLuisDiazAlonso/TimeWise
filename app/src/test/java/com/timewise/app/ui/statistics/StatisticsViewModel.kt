package com.timewise.app.ui.statistics

import com.timewise.app.domain.model.CategoryTimeStats
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.model.TimeStatsSummary
import com.timewise.app.domain.usecase.GetTimeStatsUseCase
import com.timewise.app.domain.usecase.premium.CheckPremiumAccessUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getTimeStatsUseCase: GetTimeStatsUseCase
    private lateinit var checkPremiumAccessUseCase: CheckPremiumAccessUseCase
    private lateinit var viewModel: StatisticsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTimeStatsUseCase = mockk()
        checkPremiumAccessUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun summary(totalMinutes: Long = 90L) = TimeStatsSummary(
        period = TimeStatsPeriod.SEMANAL,
        periodStart = LocalDate.of(2026, 6, 8),
        periodEnd = LocalDate.of(2026, 6, 14),
        categoryStats = listOf(
            CategoryTimeStats(categoryColorHex = "#FF0000", totalMinutes = totalMinutes, percentage = 1f)
        ),
        totalTrackedMinutes = totalMinutes
    )

    @Test
    fun `el estado inicial esta en carga`() = runTest {
        every { getTimeStatsUseCase(any(), any()) } returns flowOf(summary())
        every { checkPremiumAccessUseCase() } returns flowOf(true)
        viewModel = StatisticsViewModel(getTimeStatsUseCase, checkPremiumAccessUseCase)

        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `combina estadisticas y estado premium en el uiState`() = runTest {
        every { getTimeStatsUseCase(any(), any()) } returns flowOf(summary(totalMinutes = 120L))
        every { checkPremiumAccessUseCase() } returns flowOf(true)
        viewModel = StatisticsViewModel(getTimeStatsUseCase, checkPremiumAccessUseCase)

        val collectorJob = backgroundScope.launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(120L, state.totalMinutes)
        assertTrue(state.isPremiumUnlocked)
        assertTrue(!state.isLoading)
        collectorJob.cancel()
    }

    @Test
    fun `onPeriodSelected relanza la consulta con el nuevo periodo`() = runTest {
        every { getTimeStatsUseCase(TimeStatsPeriod.SEMANAL, any()) } returns flowOf(summary())
        every { getTimeStatsUseCase(TimeStatsPeriod.MENSUAL, any()) } returns flowOf(summary(totalMinutes = 500L))
        every { checkPremiumAccessUseCase() } returns flowOf(true)
        viewModel = StatisticsViewModel(getTimeStatsUseCase, checkPremiumAccessUseCase)
        val collectorJob = backgroundScope.launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onPeriodSelected(TimeStatsPeriod.MENSUAL)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(500L, viewModel.uiState.value.totalMinutes)
        assertEquals(TimeStatsPeriod.MENSUAL, viewModel.uiState.value.period)
        collectorJob.cancel()
    }

    @Test
    fun `onPreviousPeriod retrocede una semana en modo SEMANAL`() = runTest {
        every { getTimeStatsUseCase(any(), any()) } returns flowOf(summary())
        every { checkPremiumAccessUseCase() } returns flowOf(true)
        viewModel = StatisticsViewModel(getTimeStatsUseCase, checkPremiumAccessUseCase)
        val collectorJob = backgroundScope.launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        val today = LocalDate.now()

        viewModel.onPreviousPeriod()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(today.minusWeeks(1), viewModel.uiState.value.referenceDate)
        collectorJob.cancel()
    }

    @Test
    fun `onNextPeriod avanza un mes en modo MENSUAL`() = runTest {
        every { getTimeStatsUseCase(any(), any()) } returns flowOf(summary())
        every { checkPremiumAccessUseCase() } returns flowOf(true)
        viewModel = StatisticsViewModel(getTimeStatsUseCase, checkPremiumAccessUseCase)
        val collectorJob = backgroundScope.launch { viewModel.uiState.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()
        val today = LocalDate.now()

        viewModel.onPeriodSelected(TimeStatsPeriod.MENSUAL)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onNextPeriod()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(today.plusMonths(1), viewModel.uiState.value.referenceDate)
        collectorJob.cancel()
    }
}