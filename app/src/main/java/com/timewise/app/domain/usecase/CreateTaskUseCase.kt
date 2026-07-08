package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: String, toEpochMilli: Long?, priority: Priority, toLong: Long): Long {
        require (task.title.isNotBlank()) { "El título de la tarea no puede estar en blanco" }
        return repository.insert(task)
    }
}


