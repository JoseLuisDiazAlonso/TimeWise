package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.repository.TimeBlockRepository
import java.time.LocalTime
import javax.inject.Inject

class UpdateTimeBlockUseCase @Inject constructor(
    private val repository: TimeBlockRepository,
    private val validateTimeBlockOverlapUseCase: ValidateTimeBlockOverlapUseCase
) {
    suspend operator fun invoke(
        id: Long,
        title: String,
        startTime: LocalTime,
        endTime: LocalTime,
        colorHex: String
    ): Result<Unit> {
        require(title.isNotBlank()) { "El título del bloque no puede estar en blanco" }
        require(endTime > startTime) { "La hora de fin debe ser posterior a la de inicio" }

        val current = repository.getTimeBlockById(id)
            ?: return Result.failure(IllegalArgumentException("No existe un bloque con id $id"))

        val noOverlap = validateTimeBlockOverlapUseCase(current.date, startTime, endTime, excludeId = id)
        if (!noOverlap) {
            return Result.failure(IllegalStateException("overlap"))
        }

        repository.update(
            current.copy(
                title = title,
                startTime = startTime,
                endTime = endTime,
                colorHex = colorHex,
                updatedAt = System.currentTimeMillis()
            )
        )
        return Result.success(Unit)
    }
}