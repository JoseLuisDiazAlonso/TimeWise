package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Esta clase expone el flujo de bloques que hay en un día concreto.
 *
 * Propiedades:
 *  - repository que es de tipo TimeBlockRepository y ese Inyectado por el constructor.
 *
 *Funciones:
 *  - operator fun invoke(date: LocalDate) : Flow<List<TimeBlock>> que delega directamente
 *  en repository.observeTimeBlocksForDate(date)
 *  **/

class GetTimeBlocksForDateUseCase @Inject constructor(
    private val repository: TimeBlockRepository
) {
    operator fun invoke(date: LocalDate): Flow<List<TimeBlock>> {
        return repository.observeTimeBlocksForDate(date)
    }
}