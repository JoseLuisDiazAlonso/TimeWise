package com.timewise.app.domain.usecase.widget

import com.timewise.app.domain.ads.IsFeatureUnlockedUseCase
import com.timewise.app.domain.model.ReminderSummary
import com.timewise.app.domain.model.ReminderType
import com.timewise.app.domain.model.widget.TaskSummary
import com.timewise.app.domain.model.widget.WidgetUiState
import com.timewise.app.domain.repository.TaskRepository
import com.timewise.app.domain.repository.TimeBlockRepository
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import javax.inject.Inject

/**
 * Esta clase lo que hace es determinar el estado completo del Widget
 */
class GetWidgetDataUseCase @Inject constructor(
    private val isFeatureUnlockedUseCase: IsFeatureUnlockedUseCase,
    private val taskRepository: TaskRepository,
    private val timeBlockRepository: TimeBlockRepository,
) {
    /**
     * Determina si la función está bloqueada o no. Si no está bloqueada devuelve el
     * WidgetUiState con isUnlocked = false y listas/valores vacíos. Si está desbloqueada,
     * obtiene las tareas y bloques de hoy y calcula el recordatorio más próximo comparando
     * la hora actual con las tareas con hora y los bloques de tiempo del día.
     */
    suspend operator fun invoke(): WidgetUiState {
        val isUnlocked = isFeatureUnlockedUseCase.execute()

        return if (isUnlocked) {
            val tasks = taskRepository.getAll().first()
            val timeBlocks = timeBlockRepository.getAll().first()

            val taskSummaries = tasks.map { task ->
                TaskSummary(
                    id = task.id,
                    title = task.title,
                    isCompleted = task.isCompleted,
                    time = task.dueDate?.let { LocalTime.ofSecondOfDay(it / 1000) }
                )
            }

            val nextReminder = ReminderSummary(
                title = "Next Reminder",
                time = LocalTime.now().plusMinutes(30), // Placeholder for actual logic to find the next reminder
                type = ReminderType.TASK // Placeholder for actual logic to determine the type
            )

            WidgetUiState(
                isUnlocked = isUnlocked,
                todayTask = taskSummaries,
                nextReminder = nextReminder,
            )
        } else {
            WidgetUiState(
                isUnlocked = isUnlocked,
                todayTask = emptyList(),
                nextReminder = null,
            )
        }
    }
}