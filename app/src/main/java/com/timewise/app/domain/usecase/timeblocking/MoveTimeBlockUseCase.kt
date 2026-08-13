package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.repository.TimeBlockRepository
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

/**
 * Esta clase maneja el caso específico de mover un bloque. Recibe únicamente el id y el nuevo
 * rango de horario, reconstruye el bloque entero con los nuevos valores y lo actualiza.
 *
 * Variables:
 *  - repository de tipo TimeBlockRepository y es Inyectado por el constructor.
 *  - validateOverlap es de tipo ValidateTimeBlockOverlapUseCase y es inyectado por el constructor.
 *
 * Funciones:
 *  - suspend operator fun invoke (blockId: Long, date: LocalDate, newStart: LocalTime,
 *  newEnd: LocalTime): Result<Unit>. Esta función obtiene el bloque actual con
 *  repository.getTimeBlockById(blockId), si es null devuelve Result.failure. También
 *  valida el solape con exludeId=blockId, Si es válido, construye una copia del bloque con
 *  copy(startTime = newStart, endTime = newEnd) y lo actualiza con repository.update(newBlock).
 *  **/

class MoveTimeBlockUseCase @Inject constructor (
    private val repository: TimeBlockRepository,
    private val validateOverlap: ValidateTimeBlockOverlapUseCase
) {
    suspend fun invoke (blockId: Long, date: LocalDate, newStart: LocalTime, newEnd: LocalTime): Result<Unit> {
        val currentBlock = repository.getTimeBlockById(blockId)
        if (currentBlock == null) {
            return Result.failure(Exception("Time block not found"))
        }

        val isValid = validateOverlap(
            date,
            newStart,
            newEnd,
            blockId
        )

        return if (isValid) {
            val newBlock = currentBlock.copy(startTime = newStart, endTime = newEnd)
            repository.update(newBlock)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Time block overlaps with existing blocks"))
        }
    }
}