package com.timewise.app.domain.usecase


import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow


/**Creamos una clase en la que a partir de un id recupera el objeto completo directamente
 * desde el repositorio*/

class GetTaskByIdUseCase (private val taskRepository: TaskRepository)  {
    operator fun invoke (TaskId: Long) : Flow<Task?> {
       return taskRepository.getById(TaskId)

   }
}
