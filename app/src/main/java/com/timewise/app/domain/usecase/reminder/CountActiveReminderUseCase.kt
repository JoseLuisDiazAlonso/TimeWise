package com.timewise.app.domain.usecase.reminder

import com.timewise.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/*
Esta clase lo que hará es mostar el número de recordatorios pendientes
*
**/

class CountActiveReminderUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke () : Int {
        val now = System.currentTimeMillis()
        return taskRepository.getPending().first().count { it.reminderAt != null && it.reminderAt > now }
    }
}