package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import com.timewise.app.domain.usecase.timeblocking.SyncTaskTimeBlockUseCase
import javax.inject.Inject

class ToggleTaskCompletedUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val syncTaskTimeBlockUseCase: SyncTaskTimeBlockUseCase
) {
    suspend operator fun invoke(task: Task) {
        val updated = task.copy(isCompleted = !task.isCompleted)
        taskRepository.update(updated)
        syncTaskTimeBlockUseCase.syncCompletion(updated.id, updated.isCompleted)
    }
}