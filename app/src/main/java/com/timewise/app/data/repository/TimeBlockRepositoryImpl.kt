package com.timewise.app.data.repository

import com.timewise.app.data.local.dao.TimeBlockDao
import com.timewise.app.data.local.entity.TimeBlockEntity
import com.timewise.app.domain.model.TimeBlock
import com.timewise.app.domain.repository.TimeBlockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimeBlockRepositoryImpl @Inject constructor(
    private val timeBlockDao: TimeBlockDao
) : TimeBlockRepository {

    override fun getAll(): Flow<List<TimeBlock>> =
        timeBlockDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getByDateRange(startTime: Long, endTime: Long): Flow<List<TimeBlock>> =
        timeBlockDao.getByDateRange(startTime, endTime).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getByTask(taskId: Long): Flow<List<TimeBlock>> =
        timeBlockDao.getByTask(taskId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun insert(timeBlock: TimeBlock): Long =
        timeBlockDao.insert(TimeBlockEntity.fromDomain(timeBlock))

    override suspend fun update(timeBlock: TimeBlock) =
        timeBlockDao.update(TimeBlockEntity.fromDomain(timeBlock))

    override suspend fun delete(timeBlock: TimeBlock) =
        timeBlockDao.delete(TimeBlockEntity.fromDomain(timeBlock))

    override suspend fun deleteById(id: Long) =
        timeBlockDao.deleteById(id)
}