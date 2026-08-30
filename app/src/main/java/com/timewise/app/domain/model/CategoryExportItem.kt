package com.timewise.app.domain.model

import java.time.Duration

/**
 * Esta clase representa una fila del informe: una categoría, su tiempo dedicado y su
 * porcentaje sobre el total
 *
 * Variables
 *  - category: String
 *  - duration: Duration
 *  - percentage: Float
 */

data class CategoryExportItem (
    val category: String,
    val duration: Duration,
    val percentage: Float
)



