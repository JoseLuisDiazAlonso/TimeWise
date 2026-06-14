package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import javax.inject.Inject

class CreateTimeBlockuseCase @Inject constructor(
    private val repository: TimeBlockRepository
) {
    suspend operator fun invoke(timeBlock: TimeBlock): Long {
        require(timeBlock.title.isNotBlank()) { "El bloque debe tener un título" }
        require(timeBlock.endTime > timeBlock.startTime) {
            "El bloque debe tener una duración válida"
        }
        return repository.insert(timeBlock)

    }
}
