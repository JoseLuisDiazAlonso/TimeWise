package com.timewise.app.ui.taskform

import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.model.ReminderOption
import java.time.LocalDate
import java.time.LocalTime

data class TaskFormUiState(
    val id: Long? = null,
    val title: String = "",
    val titleError: Boolean = false,
    val description: String = "",
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val reminderOption: ReminderOption = ReminderOption.NONE,
    val priority: Priority = Priority.MEDIUM,
    val categoryOption: CategoryOption = availableCategories.first(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
    val showExactAlarmPermissionDialog: Boolean = false,
)