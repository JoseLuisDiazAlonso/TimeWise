package com.timewise.app.domain.usecase.reminder

import com.timewise.app.domain.repository.ReminderScheduler
import javax.inject.Inject

/*
Esta clase va a controlar dos funciones.
1) Cuando el usuario quita el recordatorio de una clase existente, lo edita o lo borra.
2) Cuando borra la tarea completa*
**/

class CancelReminderUseCase @Inject constructor(
    private val scheduler : ReminderScheduler,
) {
    operator fun invoke (taskId : Long) {
        scheduler.cancel(taskId)
    }
}