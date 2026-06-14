package com.timewise.app.data.local.dao

import androidx.room.*
import com.timewise.app.data.local.entity.TimeBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeBlockDao {

    @Query("SELECT * FROM time_blocks ORDER BY startTime ASC")
    fun getAll(): Flow<List<TimeBlockEntity>>

    @Query("""
        SELECT * FROM time_blocks
        WHERE startTime >= :startTime AND endTime <= :endTime
        ORDER BY startTime ASC
    """)
    fun getByDateRange(startTime: Long, endTime: Long): Flow<List<TimeBlockEntity>>

    @Query("SELECT * FROM time_blocks WHERE taskId = :taskId")  // ← añadido
    fun getByTask(taskId: Long): Flow<List<TimeBlockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timeBlock: TimeBlockEntity): Long

    @Update
    suspend fun update(timeBlock: TimeBlockEntity)

    @Delete
    suspend fun delete(timeBlock: TimeBlockEntity)

    @Query("DELETE FROM time_blocks WHERE id = :id")
    suspend fun deleteById(id: Long)
}
