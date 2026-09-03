package com.timewise.app.data.notifications

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.timewise.app.domain.repository.ReminderScheduler
import com.timewise.app.domain.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

/**
 * AlarmManager pierde todas las alarmas programadas al reiniciar el dispositivo.
 * Este worker relee las tareas con recordatorio activo y futuro, y las vuelve
 * a programar. Se dispara desde BootCompletedReceiver.
 */
@HiltWorker
class RescheduleRemindersWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val taskRepository: TaskRepository,
    private val reminderScheduler: ReminderScheduler
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!reminderScheduler.canScheduleExactAlarms()) {
            return Result.success()
        }

        return try {
            val now = System.currentTimeMillis()
            val tasks = taskRepository.getAll().first()

            tasks
                .filter { it.reminderEnabled && it.reminderAt != null && it.reminderAt > now }
                .forEach { task ->
                    reminderScheduler.schedule(task.id, task.title, task.reminderAt!!)
                }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}