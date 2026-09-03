package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.repository.TimeBlockRepository
import java.time.LocalTime
import javax.inject.Inject

class MoveTimeBlockUseCase @Inject constructor(
    private val repository: TimeBlockRepository,
    private val validateTimeBlockOverlapUseCase: ValidateTimeBlockOverlapUseCase
) {
    suspend operator fun invoke(
        id: Long,
        newStartTime: LocalTime,
        newEndTime: LocalTime
    ): Result<Unit> {
        if (newEndTime <= newStartTime) {
            return Result.failure(IllegalArgumentException("La hora de fin debe ser posterior a la de inicio"))
        }

        val current = repository.getTimeBlockById(id)
            ?: return Result.failure(IllegalArgumentException("No existe un bloque con id $id"))

        val noOverlap = validateTimeBlockOverlapUseCase(current.date, newStartTime, newEndTime, excludeId = id)
        if (!noOverlap) {
            return Result.failure(IllegalStateException("overlap"))
        }

        repository.update(
            current.copy(
                startTime = newStartTime,
                endTime = newEndTime,
                updatedAt = System.currentTimeMillis()
            )
        )
        return Result.success(Unit)
    }
}