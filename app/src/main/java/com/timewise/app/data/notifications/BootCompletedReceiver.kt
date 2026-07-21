package com.timewise.app.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.timewise.app.domain.repository.ReminderScheduler
import com.timewise.app.domain.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Recibe el broadcast BOOT_COMPLETED del sistema. Como AlarmManager pierde todas
 * las alarmas al reiniciar el dispositivo, este receiver vuelve a programar
 * el recordatorio de cada tarea que tenga uno pendiente en el futuro.
 */
@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var taskRepository: TaskRepository

    @Inject
    lateinit var reminderScheduler: ReminderScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val now = System.currentTimeMillis()
                val pendingTasks = taskRepository.getPending().first()

                pendingTasks
                    .filter { task -> task.reminderAt != null && task.reminderAt > now }
                    .forEach { task ->
                        reminderScheduler.schedule(
                            reminderId = task.id,
                            title = task.title,
                            triggerAtMillis = task.reminderAt!!,
                        )
                    }
            } finally {
                pendingResult.finish()
            }
        }
    }
}