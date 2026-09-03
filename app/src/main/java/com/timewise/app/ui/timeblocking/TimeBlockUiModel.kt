package com.timewise.app.ui.timeblocking

data class TimeBlockUiModel(
    val id: Long,
    val taskId: Long?,
    val title: String,
    val categoryColor: String,
    val offsetMinutes: Int,
    val durationMinutes: Int,
    val isCompleted: Boolean
)