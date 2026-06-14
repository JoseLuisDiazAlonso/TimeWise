package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
){
    operator fun invoke(): Flow<List<Task>> {
        return repository.getAll()
    }
}
