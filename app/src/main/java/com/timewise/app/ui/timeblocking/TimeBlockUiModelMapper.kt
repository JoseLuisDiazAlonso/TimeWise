package com.timewise.app.ui.timeblocking

import com.timewise.app.domain.model.TimeBlock
import java.time.LocalTime

fun TimeBlock.toUiModel(): TimeBlockUiModel {
    val offsetMinutes = startTime.hour * 60 + startTime.minute
    val endMinutes = endTime.hour * 60 + endTime.minute
    return TimeBlockUiModel(
        id = id,
        taskId = taskId,
        title = title,
        categoryColor = colorHex,
        offsetMinutes = offsetMinutes,
        durationMinutes = (endMinutes - offsetMinutes).coerceAtLeast(0),
        isCompleted = isCompleted
    )
}

fun minutesToLocalTime(minutes: Int): LocalTime {
    val clamped = minutes.coerceIn(0, 23 * 60 + 59)
    return LocalTime.of(clamped / 60, clamped % 60)
}