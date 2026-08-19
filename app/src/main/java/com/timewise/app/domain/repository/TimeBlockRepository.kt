package com.timewise.app.domain.repository

import com.timewise.app.domain.model.TimeBlock
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

interface TimeBlockRepository {


    fun getAll(): Flow<List<TimeBlock>>
    fun getByDateRange(startTime: Long, endTime: Long): Flow<List<TimeBlock>>
    fun getByTask(taskId: Long): Flow<List<TimeBlock>>


    fun observeTimeBlocksForDate(date: LocalDate): Flow<List<TimeBlock>>


    suspend fun getTimeBlockById(id: Long): TimeBlock?

    suspend fun getOverlappingBlocks(
        date: LocalDate,
        startTime: LocalTime,
        endTime: LocalTime,
        excludeId: Any? = null
    ): List<TimeBlock>


    suspend fun insert(timeBlock: TimeBlock): Long
    suspend fun update(timeBlock: TimeBlock)
    suspend fun delete(timeBlock: TimeBlock)
    suspend fun deleteById(id: Long)
    suspend fun deleteTimeBlock(id: Long)

    fun observeByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TimeBlock>>
}