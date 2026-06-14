package com.timewise.app.domain.repository

import com.timewise.app.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAll (): Flow<List<Task>>
    fun getById (id: Long): Flow<Task?>
    fun getByCategory (categoryId: Long): Flow<List<Task>>
    fun getPending (): Flow<List<Task>>

    suspend fun insert (task: Task): Long
    suspend fun update (task: Task)
    suspend fun delete (task: Task)
    suspend fun deleteById (id: Long)
    suspend fun markAsCompleted (id: Long)
}