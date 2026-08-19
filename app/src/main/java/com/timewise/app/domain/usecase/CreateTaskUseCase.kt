package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        title: String,
        dueDate: Long?,
        priority: Priority,
        categoryId: Long
    ): Long {
        require(title.isNotBlank()) { "El título de la tarea no puede estar en blanco" }
        val task = Task(
            title = title,
            dueDate = dueDate,
            priority = priority,
            categoryId = categoryId
        )
        return repository.insert(task)
    }
}