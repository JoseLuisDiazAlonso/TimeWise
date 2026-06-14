package com.timewise.app.data.repository

import com.timewise.app.data.local.dao.TaskDao
import com.timewise.app.data.local.entity.TaskEntity
import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAll(): Flow<List<Task>> =
        taskDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override fun getById(id: Long): Flow<Task?> =
        taskDao.getById(id).map { it?.toDomain() }

    override fun getByCategory(categoryId: Long): Flow<List<Task>> =
        taskDao.getByCategory(categoryId).map { entities -> entities.map { it.toDomain() } }

    override fun getPending(): Flow<List<Task>> =
        taskDao.getPending().map { entities -> entities.map { it.toDomain() } }

    override suspend fun insert(task: Task): Long =
        taskDao.insert(TaskEntity.fromDomain(task))

    override suspend fun update(task: Task) =
        taskDao.update(TaskEntity.fromDomain(task))

    override suspend fun delete(task: Task) =
        taskDao.delete(TaskEntity.fromDomain(task))

    override suspend fun deleteById(id: Long) =
        taskDao.deleteById(id)

    override suspend fun markAsCompleted(id: Long) =
        taskDao.markAsCompleted(id)
}