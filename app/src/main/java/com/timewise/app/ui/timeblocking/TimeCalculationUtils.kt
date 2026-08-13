package com.timewise.app.ui.timeblocking

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Este objeto representa las funciones puras que traducen entre minutos y dp. Al ser
 * funciones puras y sin dependencias de Android más allá de Dp.
 *
 * Funciones:
 *  - fun minutestoDp (minutes: Int, hourHeightDp Int= TimeGridconstants.HOUR_HEIGHT_DP): Dp.
 *  Esta función se usa para devolver (minutos/60f*hourHeightDp).dp. Se usa para calcular la posición vertical de un bloque en la rejilla.
 *  - fun dpToMinutes (offset: Dp, hourheightDp: Int =
 *  TimeGridConstants.HOUR_HEIGHT_DP): Int. Esta función es la operación inversa.
 *  - fun snapToGrid (minutes: Int, snapMinutes: Int = TimeGridConstants.SNAP_MINUTES): Int.
 *  Redondea al múltiple de snapMinutes más cercano.
 *  **/

object TimeCalculationUtils {
    fun minutesToDp(minutes: Int, hourHeightDp: Int = TimeGridConstants.HOUR_HEIGHT_DP): Dp {
        return (minutes / 60f * hourHeightDp).dp
    }

    fun dpToMinutes(offset: Dp, hourHeightDp: Int = TimeGridConstants.HOUR_HEIGHT_DP): Int {
        return (offset.value / hourHeightDp * 60).toInt()
    }

    fun snapToGrid(minutes: Int, snapMinutes: Int = TimeGridConstants.SNAP_MINUTES): Int {
        val remainder = minutes % snapMinutes
        return if (remainder < snapMinutes / 2) {
            minutes - remainder
        } else {
            minutes + (snapMinutes - remainder)
        }
    }
}