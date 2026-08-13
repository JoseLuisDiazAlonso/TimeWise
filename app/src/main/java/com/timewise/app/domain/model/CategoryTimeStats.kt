package com.timewise.app.domain.model

/**
 * Esta clase muestra el resultado agregado para una única categoría determinado el tiempo
 * que se le dedicó y que porcentaje representa sobre el total del periodo.
 * **/

data class CategoryTimeStats (
    val categoryColorHex: String = "#6200EE",
    val totalMinutes: Long,
    val percentage: Float
)
