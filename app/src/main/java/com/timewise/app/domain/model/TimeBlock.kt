package com.timewise.app.domain.model

import java.time.LocalDate

data class TimeBlock(
    val id: Long = 0,
    val taskId: Long? = null,
    val title: String,
    val date: LocalDate,
    val startTime: Long,
    val endTime: Long,
    val colorHex: String = "#6200EE",
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null,
    val iscompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean,
    val durationMinutes: Unit
) {

}