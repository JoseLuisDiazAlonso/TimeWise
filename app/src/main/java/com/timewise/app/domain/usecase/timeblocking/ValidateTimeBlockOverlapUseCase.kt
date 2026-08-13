package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.repository.TimeBlockRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Esta clase centraliza la regla de negocio en la cual dos bloques del mismo día no pueden
 * solaparse.
 *
 * Las Propiedades son:
 *  - repository que es de tipo TimeBlockRepository y es Inyectado por el constructor.
 *
 *  Funciones:
 *      -suspend operator fun invoke (date: LocalDate, start: Long, end: Long,
 *      excluideId: Long? = null) : Boolean. Devuelve True si no hay solapes y False si los hay
 * **/

class ValidateTimeBlockOverlapUseCase @Inject constructor(
    private val repository: TimeBlockRepository,
    private val validateTimeBlockOverlapUseCase: ValidateTimeBlockOverlapUseCase
) {
    val id: Any = TODO()
    val endTime: Long
        get() {
            TODO()
        }
    val startTime: Long
    val date: LocalDate

    suspend operator fun invoke (date: LocalDate, start: LocalTime, end: LocalTime, excludeId: Any? = null): Boolean {
        val overlappingBlocks = repository.getOverlappingBlocks(date, start, end, excludeId)
        return overlappingBlocks.isEmpty()
    }
    
}
