package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Long {
        require(task.title.isNotBlank()) { "El título de la tarea no puede estar en blanco" }
        return repository.insert(task)
    }
}

