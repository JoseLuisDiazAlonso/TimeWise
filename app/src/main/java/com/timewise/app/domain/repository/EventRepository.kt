package com.timewise.app.domain.repository

import com.timewise.app.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    fun getAll (): Flow<List<Event>>
    fun getById (id: Long): Flow<Event?>
    fun getByDateRange (startTime: Long, endTime: Long): Flow<List<Event>>
    fun getUpcoming (fromTime: Long = System.currentTimeMillis()): Flow<List<Event>>

    suspend fun insert (event: Event): Long
    suspend fun update (event: Event)
    suspend fun delete (event: Event)
    suspend fun deleteById (id: Long)
}
