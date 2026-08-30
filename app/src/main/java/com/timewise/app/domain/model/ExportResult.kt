package com.timewise.app.domain.model

/**
 * Resultado tipado de la operación de exportación
 * Variables
 *  - Success: ExportResult
 *  - PremiumRequired: ExportResult
 *  - Error (val message: String): ExportResult
 */

sealed class ExportResult {
    data object Success : ExportResult()
    data object PremiumRequired : ExportResult()
    data class Error(val message: String) : ExportResult()
}