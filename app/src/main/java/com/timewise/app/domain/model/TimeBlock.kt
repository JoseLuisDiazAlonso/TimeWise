package com.timewise.app.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class TimeBlock(
    val id: Long = 0,
    val taskId: Long? = null,
    val title: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val colorHex: String = "#6200EE",
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)