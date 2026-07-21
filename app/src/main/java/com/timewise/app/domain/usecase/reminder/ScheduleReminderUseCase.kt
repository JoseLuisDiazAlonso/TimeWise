package com.timewise.app.domain.usecase.reminder

import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.ReminderScheduler
import javax.inject.Inject

/*
Esta clase lo que hará es determinar si existe el modo de pago o premium para
determinar si le corresponden cinco o más recordatorios.*
**/

sealed interface ScheduleReminderResult {
    data object Scheduled: ScheduleReminderResult
    data object FreeLimitReached: ScheduleReminderResult
}

private const val MAX_FREE_REMINDERS = 5


class ScheduleReminderUseCase @Inject constructor(
    private val scheduler : ReminderScheduler,
    private val countReminderUseCase : CountActiveReminderUseCase,
    //private val userPreferencesRepository : UserPreferencesRepository, //
) {

    suspend operator fun invoke (task : Task) : ScheduleReminderResult {
        val reminderAt = task.reminderAt ?: return ScheduleReminderResult.Scheduled

        val isPremium = false
        if (!isPremium && countReminderUseCase ()  >= MAX_FREE_REMINDERS) {
            return ScheduleReminderResult.FreeLimitReached
        }
        scheduler.schedule (task.id, task.title, reminderAt)
        return ScheduleReminderResult.Scheduled
    }
}