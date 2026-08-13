package com.timewise.app.ui.timeblocking

/**
 * Representación de un bloque ya preparada para dibujarse. En lugar de LocalTime,
 * expone offsets en minutos desde las 00:00 horas, listos para pasar por minutesToDp().
 * De esta forma se evita recalcular estos valores dentro del propio Composable en cada
 * recomposición.
 *
 * Propiedades:
 *  - id: Long
 *  - title: String
 *  - categoryColor: String
 *  - offsetMinutes: Int
 *  - durationMinutes: Int
 *  - isCompleted: Boolean
 **/
data class TimeBlockUiModel(
    val id: Long,
    val title: String,
    val categoryColor: String,
    val offsetMinutes: Int,
    val durationMinutes: Int,
    val isCompleted: Boolean
)