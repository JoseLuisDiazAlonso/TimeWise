package com.timewise.app.ui.taskform

import com.timewise.app.domain.model.Priority
import java.time.LocalDate
import java.time.LocalTime

data class TaskFormUiState (
    val id: Long? = null,
    val title: String = "",
    val titleError: Boolean = false,
    val description: String = "",
    val dueDate: LocalDate? = null,
    val dueTime: LocalTime? = null,
    val priority: Priority = Priority.MEDIUM,
    val categoryOption: CategoryOption = availableCategories.first(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val isDeleted: Boolean = false,
)