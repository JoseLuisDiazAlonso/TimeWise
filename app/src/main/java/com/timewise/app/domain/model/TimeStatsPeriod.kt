package com.timewise.app.domain.model

/**
 * Es un Enum que representa el tipo de periodo que el usuario está consultando.
 * Sustituye el booleano "isWeekly" por algo más explicitito y extensible
 *
 * Funciones:
 *  - Semanal. Rango de siete dias de L a D, según la semana ISO
 *  - Mensual. Mes calendario completo, del día 1 al último día del mes.**/

enum class TimeStatsPeriod (val period: String) {
    SEMANAL("semanal"),
    MENSUAL("mensual");

}