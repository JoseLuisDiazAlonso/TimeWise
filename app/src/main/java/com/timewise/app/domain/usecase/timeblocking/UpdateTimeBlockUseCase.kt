package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import javax.inject.Inject

/**
 * Esta clase actualiza un bloque ya existente, validando de nuevo el solape
 * excluyendo el propio bloque.
 *
 * Propiedades:
 *  - repository: TimeBlockRepository, inyectado por constructor.
 *  - validateOverlap: ValidateTimeBlockOverlapUseCase, inyectado por constructor.
 *
 * Funciones:
 *  - suspend operator fun invoke(timeBlock: TimeBlock): Result<Unit>
 **/
class UpdateTimeBlockUseCase @Inject constructor(
    private val repository: TimeBlockRepository,
    private val validateOverlap: ValidateTimeBlockOverlapUseCase
) {
    suspend operator fun invoke(timeBlock: TimeBlock): Result<Unit> {
        val isValid = validateOverlap(
            timeBlock.date,
            timeBlock.startTime,
            timeBlock.endTime,
            timeBlock.id.takeIf { it != 0L }
        )

        return if (isValid) {
            repository.update(timeBlock)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Time block overlaps with existing blocks"))
        }
    }
}
