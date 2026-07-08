package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke (
        task: Long?,
        title: String,
        toEpochMilli: Long?,
        priority: Priority,
        toLong: Long
    ) {
        require (task.title.isNotBlank()) { "El título de la tarea no puede estar en blanco" }
        repository.update(task.copy(updatedAt = System.currentTimeMillis()))
    }
    }
