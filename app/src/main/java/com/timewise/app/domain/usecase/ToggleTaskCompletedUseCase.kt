package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskCompletedUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        taskRepository.update(task.copy(isCompleted = !task.isCompleted))
    }
}