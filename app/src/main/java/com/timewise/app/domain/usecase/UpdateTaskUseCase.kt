package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke (task:Task) {
        require (task.title.isNotBlank()) { "El título de la tarea no puede estar en blanco" }
        repository.update(task.copy(updatedAt = System.currentTimeMillis()))
    }
    }
