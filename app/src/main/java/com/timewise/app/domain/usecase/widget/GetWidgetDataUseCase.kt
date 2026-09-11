package com.timewise.app.domain.usecase.widget

import com.timewise.app.domain.ads.IsFeatureUnlockedUseCase
import com.timewise.app.domain.model.ReminderSummary
import com.timewise.app.domain.model.ReminderType
import com.timewise.app.domain.model.Task
import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.model.widget.TaskSummary
import com.timewise.app.domain.model.widget.WidgetUiState
import com.timewise.app.domain.repository.TaskRepository
import com.timewise.app.domain.repository.TimeBlockRepository
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Determina el estado completo que el widget debe mostrar en un momento dado:
 * si la función está desbloqueada, las tareas de hoy, y el próximo recordatorio
 * (tarea o bloque de tiempo) más cercano a la hora actual.
 */
class GetWidgetDataUseCase @Inject constructor(
    private val isFeatureUnlockedUseCase: IsFeatureUnlockedUseCase,
    private val taskRepository: TaskRepository,
    private val timeBlockRepository: TimeBlockRepository,
) {
    suspend operator fun invoke(): WidgetUiState {
        val isUnlocked = isFeatureUnlockedUseCase.execute()

        if (!isUnlocked) {
            return WidgetUiState(
                isUnlocked = false,
                todayTask = emptyList(),
                nextReminder = null
            )
        }

        val today = LocalDate.now()

        val todaysTasks = taskRepository.getAll().first()
            .filter { task -> task.dueDate?.toLocalDate() == today }

        val todaysTimeBlocks = timeBlockRepository.getAll().first()
            .filter { block -> block.date == today }

        val taskSummaries = todaysTasks.map { task ->
            TaskSummary(
                id = task.id,
                title = task.title,
                isCompleted = task.isCompleted,
                time = task.dueDate?.toLocalTime()
            )
        }

        val nextReminder = findNextReminder(todaysTasks, todaysTimeBlocks)

        return WidgetUiState(
            isUnlocked = true,
            todayTask = taskSummaries,
            nextReminder = nextReminder
        )
    }

    /**
     * Busca el próximo evento pendiente del día (tarea con hora o bloque de tiempo)
     * comparando contra la hora actual, y se queda con el más cercano que aún no ha pasado.
     */
    private fun findNextReminder(
        tasks: List<Task>,
        timeBlocks: List<TimeBlock>
    ): ReminderSummary? {
        val now = LocalTime.now()

        val taskCandidates = tasks
            .filter { !it.isCompleted }
            .mapNotNull { task ->
                val time = task.dueDate?.toLocalTime() ?: return@mapNotNull null
                if (time.isBefore(now)) return@mapNotNull null
                ReminderSummary(title = task.title, time = time, type = ReminderType.TASK)
            }

        val timeBlockCandidates = timeBlocks
            .filter { !it.isCompleted && !it.startTime.isBefore(now) }
            .map { block ->
                ReminderSummary(title = block.title, time = block.startTime, type = ReminderType.TIMEBLOCK)
            }

        return (taskCandidates + timeBlockCandidates)
            .minByOrNull { it.time }
    }

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

    private fun Long.toLocalTime(): LocalTime =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalTime()
}