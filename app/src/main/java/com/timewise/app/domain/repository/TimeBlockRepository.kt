package com.timewise.app.domain.repository

import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.model.TimeStatsSummary
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TimeBlockRepository {


    fun getAll(): Flow<List<TimeBlock>>
    fun getByDateRange(startTime: Long, endTime: Long): Flow<List<TimeBlock>>
    fun getByTask(taskId: Long): Flow<List<TimeBlock>>


    fun observeTimeBlocksForDate(date: LocalDate): Flow<List<TimeBlock>>


    suspend fun getTimeBlockById(id: Long): TimeBlock?

    suspend fun getOverlappingBlocks(
        date: LocalDate,
        startTime: Long,
        endTime: Long,
        excludeId: Any? = null
    ): List<TimeBlock>


    suspend fun insert(timeBlock: TimeBlock): Long
    suspend fun update(timeBlock: TimeBlock)
    suspend fun delete(timeBlock: TimeBlock)
    suspend fun deleteById(id: Long)
    suspend fun deleteTimeBlock(id: Long)

    fun observeByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TimeBlock>>
}