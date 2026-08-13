package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.repository.TimeBlockRepository
import javax.inject.Inject

/**
 * Esta clase elimina un bloque de la base de datos.
 *
 * Variables:
 *  - repository de tipo TimeBlockRepository y es Inyectado por el constructor.
 *
 * Funciones:
 *  - suspend operator fun invoke (id: Long): Unit. Esta función delega en repository.deleteTimeBock(id=
 ***/

class DeleteTimeBlockUseCase @Inject constructor(
    private val repository: TimeBlockRepository
) {
    suspend operator fun invoke (id: Long) {
        repository.deleteTimeBlock(id)
    }
}