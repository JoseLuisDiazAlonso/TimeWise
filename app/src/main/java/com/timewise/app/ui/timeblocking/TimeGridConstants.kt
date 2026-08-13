package com.timewise.app.ui.timeblocking

/**
 * Este objeto contiene las constantes de la rejilla horaria, compartida por el grid, el
 * cálculo de posiciones y el gesto de arrastre. Centralizándose aqui evita "números mágicos"
 * repartido por los composables.
 *
 * Funciones:
 *  - const val START_HOUR: Int = 0. Que es la primera de la rejilla visible
 *  - const val END_HOUR: Int = 24. Última hora visible.
 *  - const HOUR_HEIGHT_DP: Int = 64. Altura en dp que ocupa una hora completa en pantalla.
 *  - const val SNAP_MINUTES: Int = 15. Granularidad de "iman" al soltar un bloque.
 *  **/

object TimeGridConstants {
    const val START_HOUR: Int = 0
    const val END_HOUR: Int = 24
    const val HOUR_HEIGHT_DP: Int = 64
    const val SNAP_MINUTES: Int = 15
}