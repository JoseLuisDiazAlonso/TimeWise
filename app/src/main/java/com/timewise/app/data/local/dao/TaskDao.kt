package com.timewise.app.data.local.dao

import androidx.room.*
import com.timewise.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getById(id: Long): Flow<TaskEntity?>

    @Query("SELECT * FROM tasks WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getPending(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE tasks SET isCompleted = 1, updatedAt = :updatedAt WHERE id = :id")
    suspend fun markAsCompleted(id: Long, updatedAt: Long = System.currentTimeMillis())
}


