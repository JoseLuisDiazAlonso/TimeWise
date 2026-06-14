package com.timewise.app.domain.repository

import com.timewise.app.domain.model.TimeBlock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TimeBlockRepository {
    fun getAll (): Flow<List<TimeBlock>>
    fun getByDateRange (starTime: Long, endTime: Long): Flow<List<TimeBlock>>
    fun getByTask (taskId: Long): Flow<List<TimeBlock>>

    suspend fun insert (timeBlock: TimeBlock): Long
    suspend fun update (timeBlock: TimeBlock)
    suspend fun delete (timeBlock: TimeBlock)
    suspend fun deleteById (id: Long)
}