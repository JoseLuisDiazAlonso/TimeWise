package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Mantiene sincronizado el TimeBlock vinculado a una tarea (TimeBlock.taskId).
 * Sincronización de un solo sentido: Task -> TimeBlock. Editar directamente el
 * bloque en el calendario (solo horas) NO actualiza la tarea original.
 */
class SyncTaskTimeBlockUseCase @Inject constructor(
    private val timeBlockRepository: TimeBlockRepository
) {
    private val defaultDurationMinutes = 30L

    suspend fun syncForTask(
        taskId: Long,
        title: String,
        dueDateMillis: Long?,
        colorHex: String,
        isCompleted: Boolean = false
    ) {
        if (dueDateMillis == null) {
            removeForTask(taskId)
            return
        }

        val zoned = Instant.ofEpochMilli(dueDateMillis).atZone(ZoneId.systemDefault())
        val date = zoned.toLocalDate()
        val startTime = zoned.toLocalTime()

        val existing = timeBlockRepository.getByTask(taskId).first().firstOrNull()

        if (existing == null) {
            val endTime = safeEndTime(startTime, defaultDurationMinutes)
            timeBlockRepository.insert(
                TimeBlock(
                    taskId = taskId,
                    title = title,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    colorHex = colorHex,
                    isCompleted = isCompleted
                )
            )
        } else {
            // Si el usuario ya había ajustado la duración a mano en el calendario,
            // se conserva; solo se recalcula si quedaría inválida (fin <= inicio).
            val preservedMinutes = Duration.between(existing.startTime, existing.endTime).toMinutes()
            val durationToUse = if (preservedMinutes > 0) preservedMinutes else defaultDurationMinutes
            val endTime = safeEndTime(startTime, durationToUse)

            timeBlockRepository.update(
                existing.copy(
                    title = title,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    colorHex = colorHex,
                    isCompleted = isCompleted,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun removeForTask(taskId: Long) {
        timeBlockRepository.getByTask(taskId).first().forEach {
            timeBlockRepository.deleteById(it.id)
        }
    }

    suspend fun syncCompletion(taskId: Long, isCompleted: Boolean) {
        val existing = timeBlockRepository.getByTask(taskId).first().firstOrNull() ?: return
        timeBlockRepository.update(
            existing.copy(isCompleted = isCompleted, updatedAt = System.currentTimeMillis())
        )
    }

    private fun safeEndTime(start: LocalTime, minutes: Long): LocalTime {
        val end = start.plusMinutes(minutes)
        return if (end < start) LocalTime.of(23, 59) else end // evita cruzar medianoche
    }
}