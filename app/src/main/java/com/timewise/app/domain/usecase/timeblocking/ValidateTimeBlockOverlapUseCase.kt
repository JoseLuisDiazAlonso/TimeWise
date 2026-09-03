package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.repository.TimeBlockRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Centraliza la regla de negocio: dos bloques del mismo día no pueden solaparse.
 */
class ValidateTimeBlockOverlapUseCase @Inject constructor(
    private val repository: TimeBlockRepository
) {
    suspend operator fun invoke(
        date: LocalDate,
        start: LocalTime,
        end: LocalTime,
        excludeId: Any? = null
    ): Boolean {
        return repository.getOverlappingBlocks(date, start, end, excludeId).isEmpty()
    }
}
