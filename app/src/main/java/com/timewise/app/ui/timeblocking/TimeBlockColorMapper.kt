package com.timewise.app.ui.timeblocking

import androidx.compose.ui.graphics.toArgb
import com.timewise.app.ui.taskform.CategoryOption
import com.timewise.app.ui.taskform.availableCategories

/**
 * TimeBlock guarda colorHex (String), pero CategoryOption usa Color (Compose).
 * Este mapeador convierte entre ambos sin tocar ninguna de las dos clases.
 */
fun CategoryOption.toHexString(): String {
    val argb = this.color.toArgb()
    return String.format("#%06X", 0xFFFFFF and argb)
}

fun categoryOptionForHex(hex: String): CategoryOption =
    availableCategories.find { it.toHexString().equals(hex, ignoreCase = true) }
        ?: availableCategories.first()