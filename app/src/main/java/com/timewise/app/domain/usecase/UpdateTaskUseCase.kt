package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        id: Long,
        title: String,
        description: String,
        dueDate: Long?,
        reminderAt: Long?,
        priority: Priority,
        categoryId: Long
    ) {
        require(title.isNotBlank()) { "El título de la tarea no puede estar en blanco" }
        val currentTask = repository.getById(id).first()
            ?: throw IllegalArgumentException("No existe una tarea con id $id")

        val updatedTask = currentTask.copy(
            title = title,
            description = description,
            dueDate = dueDate,
            reminderAt = reminderAt,
            reminderEnabled = if (reminderAt != null) true else currentTask.reminderEnabled,
            priority = priority,
            categoryId = categoryId,
            updatedAt = System.currentTimeMillis()
        )
        repository.update(updatedTask)
    }
}