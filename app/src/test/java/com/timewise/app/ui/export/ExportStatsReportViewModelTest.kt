package com.timewise.app.ui.export

import android.net.Uri
import com.timewise.app.domain.model.ExportResult
import com.timewise.app.domain.usecase.ExportStatsReportUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExportStatsReportViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var exportStatsReportUseCase: ExportStatsReportUseCase
    private lateinit var viewModel: ExportStatsReportViewModel
    private val uri: Uri = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        exportStatsReportUseCase = mockk()
        viewModel = ExportStatsReportViewModel(exportStatsReportUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `estado inicial es Idle`() {
        assertTrue(viewModel.uiState.value is ExportUiState.Idle)
    }

    @Test
    fun `onExportRequested termina en Success`() = runTest {
        coEvery { exportStatsReportUseCase(uri) } returns ExportResult.Success

        viewModel.onExportRequested(uri)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ExportUiState.Success)
    }

    @Test
    fun `onExportRequested emite PremiumRequired si esta bloqueada`() = runTest {
        coEvery { exportStatsReportUseCase(uri) } returns ExportResult.PremiumRequired

        viewModel.onExportRequested(uri)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value is ExportUiState.PremiumRequired)
    }

    @Test
    fun `onExportRequested emite Error con el mensaje`() = runTest {
        coEvery { exportStatsReportUseCase(uri) } returns ExportResult.Error("Fallo de E/S")

        viewModel.onExportRequested(uri)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value as ExportUiState.Error
        assertEquals("Fallo de E/S", state.message)
    }

    @Test
    fun `consumeState vuelve a Idle`() = runTest {
        coEvery { exportStatsReportUseCase(uri) } returns ExportResult.Success
        viewModel.onExportRequested(uri)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.consumeState()

        assertTrue(viewModel.uiState.value is ExportUiState.Idle)
    }
}