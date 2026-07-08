package com.timewise.app.data.repository

import com.timewise.app.data.local.dao.EventDao
import com.timewise.app.data.local.entity.EventEntity
import com.timewise.app.domain.model.Event
import com.timewise.app.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {

    override fun getAll(): Flow<List<Event>> =
        eventDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getById(id: Long): Flow<Event?> =
        eventDao.getById(id).map { it?.toDomain() }

    override fun getByDateRange(startTime: Long, endTime: Long): Flow<List<Event>> =
        eventDao.getByDateRange(startTime, endTime).map { entities -> entities.map { it.toDomain() } }

    override fun getUpcoming(fromTime: Long): Flow<List<Event>> =
        eventDao.getUpcoming(fromTime).map { entities -> entities.map { it.toDomain() } }

    override suspend fun insert(event: Event): Long =
        eventDao.insert(EventEntity.fromDomain(event))

    override suspend fun update(event: Event) =
        eventDao.update(EventEntity.fromDomain(event))

    override suspend fun delete(event: Event) =
        eventDao.delete(EventEntity.fromDomain(event))

    override suspend fun deleteById(id: Long) =
        eventDao.deleteById(id)
}