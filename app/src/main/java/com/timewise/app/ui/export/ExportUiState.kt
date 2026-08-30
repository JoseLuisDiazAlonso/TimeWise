package com.timewise.app.ui.export

/**
 * Esta clase mapea 1:1 con ExportResult más el estado transitorio de carga.*
 * Variables
 *  - Idle:ExportUiState
 *  - Loading:ExportUiState
 *  - Success:ExportUiState
 *  - PremiumRequired:ExportUiState
 *  - Error (val message: String):ExportUiState
 */

sealed class ExportUiState {
    data object Idle : ExportUiState()
    data object Loading : ExportUiState()
    data object Success : ExportUiState()
    data object PremiumRequired : ExportUiState()
    data class Error(val message: String) : ExportUiState()
}
