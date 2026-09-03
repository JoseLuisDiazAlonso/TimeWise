package com.timewise.app.domain.usecase.timeblocking

import com.timewise.app.domain.repository.TimeBlockRepository
import javax.inject.Inject

class DeleteTimeBlockUseCase @Inject constructor(
    private val repository: TimeBlockRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteById(id)
    }
}