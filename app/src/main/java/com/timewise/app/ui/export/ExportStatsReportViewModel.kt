package com.timewise.app.ui.export


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.ExportResult
import com.timewise.app.domain.usecase.ExportStatsReportUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Esta clase es el punto de entrada desde la Screen. Recibe un Uri ya resuelto (nunca lanza
 * intents) y traduce ExportResult a ExportUiState.
 *
 * Constructor
 * @Inject constructor(private val exportStastsReportUseCase: ExportStatsReportUseCase)
 * Variables
 *  - private val _uiState = MutableStateFlow<ExportUiState>(ExportUiState.Idle)
 *  - val uiState = _uiState.asStateFlow()
 * Funciones
 *  - fun onExportRequested(uri: Uri) que lanza viewModelScope.launch
 *  - fun consumeState() vuelve a Idle tras mostrar Snackbar/paywall (evita reprocesar en
 *  recomposición.
 */

@HiltViewModel
class ExportStatsReportViewModel @Inject constructor(private val exportStastsReportUseCase: ExportStatsReportUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<ExportUiState>(ExportUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onExportRequested(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = ExportUiState.Loading
            when (val result = exportStastsReportUseCase(uri)) {
                is ExportResult.Success -> _uiState.value = ExportUiState.Success
                is ExportResult.PremiumRequired -> _uiState.value = ExportUiState.PremiumRequired
                is ExportResult.Error -> _uiState.value = ExportUiState.Error(result.message)
            }
        }
    }

    fun consumeState() {
        _uiState.value = ExportUiState.Idle
    }

}


