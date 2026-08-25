package com.timewise.app.domain.model.widget

import java.time.LocalTime

/**
 * Esta data class lo que hace es mostrar una fila concreta del widget sin tener que arrastrar
 * campos del domain completo.
 */
data class TaskSummary (
    val id: Long,
    val title: String,
    val isCompleted: Boolean,
    val time: LocalTime?
)
