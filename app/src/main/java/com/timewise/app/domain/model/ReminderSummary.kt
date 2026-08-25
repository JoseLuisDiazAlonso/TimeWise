package com.timewise.app.domain.model

import java.time.LocalTime

/**
 * Esta clase representa el siguiente evento más cercano en el tiempo. Lo que hace es
 * comparar los dos eventos y escoger el más próximo en el tiempo.
 */

data class ReminderSummary (
    val title: String,
    val time: LocalTime,
    val type: ReminderType
)
