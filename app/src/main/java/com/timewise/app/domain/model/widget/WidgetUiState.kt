package com.timewise.app.domain.model.widget

import com.timewise.app.domain.model.ReminderSummary

/**
 * Esta clase representa el estado completo que el widget debe de mostrar en un momento dado.
 */

data class WidgetUiState(
    val isUnlocked: Boolean,
    val todayTask: List<TaskSummary>,
    val nextReminder: ReminderSummary?
)