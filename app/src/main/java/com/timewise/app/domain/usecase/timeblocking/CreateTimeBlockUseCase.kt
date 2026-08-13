package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import javax.inject.Inject

/**
 * Esta clase crea un bloque nuevo tras validar que no se solapa ninguno.
 *
 * Propiedades:
 *  - repository de tipo TimeBlockRepository y es Inyectado por el constructor.
 *  - validateOverlap es de tipo ValidateTimeBlockOverlapUseCase y es inyectado por el constructor.
 *
 * Funciones:
 *  - suspend operator fun invoke (timeBlock : TimeBlock): Result<Long>
 * **/

class CreateTimeBlockUseCase @Inject constructor(
    private val repository: TimeBlockRepository,
    private val validateTimeBlockOverlapUseCase: ValidateTimeBlockOverlapUseCase
) {
    suspend operator fun invoke (timeBlock : TimeBlock): Result<Long> {
        val isValid = validateTimeBlockOverlapUseCase(
            timeBlock.date,
            timeBlock.startTime,
            timeBlock.endTime,
            timeBlock.id.takeIf { it != 0L }
        )

        return if (isValid) {
            val id = repository.insert(timeBlock)
            Result.success(id)
        } else {
            Result.failure(Exception("Time block overlaps with existing blocks"))
        }
    }
}