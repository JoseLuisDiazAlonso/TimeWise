package com.timewise.app.domain.usecase

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTimeBlocksUseCase @Inject constructor(
    private val repository: TimeBlockRepository
) {
    operator fun invoke (startTime: Long, endTime: Long): Flow<List<TimeBlock>> =
        repository.getByDateRange(startTime, endTime)
}
