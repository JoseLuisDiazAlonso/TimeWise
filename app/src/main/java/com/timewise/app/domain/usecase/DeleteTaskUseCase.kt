package com.timewise.app.domain.usecase

import com.timewise.app.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke (taskId: Long) = repository.deleteById(taskId)
}