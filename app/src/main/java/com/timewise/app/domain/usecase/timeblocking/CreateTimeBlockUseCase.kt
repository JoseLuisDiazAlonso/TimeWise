package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class CreateTimeBlockUseCase @Inject constructor(
    private val repository: TimeBlockRepository,
    private val validateTimeBlockOverlapUseCase: ValidateTimeBlockOverlapUseCase
) {
    suspend operator fun invoke(
        title: String,
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        colorHex: String
    ): Result<Long> {
        require(title.isNotBlank()) { "El título del bloque no puede estar en blanco" }
        require(endTime > startTime) { "La hora de fin debe ser posterior a la de inicio" }

        val noOverlap = validateTimeBlockOverlapUseCase(date, startTime, endTime)
        if (!noOverlap) {
            return Result.failure(IllegalStateException("overlap"))
        }

        val id = repository.insert(
            TimeBlock(
                title = title,
                date = date,
                startTime = startTime,
                endTime = endTime,
                colorHex = colorHex
            )
        )
        return Result.success(id)
    }
}