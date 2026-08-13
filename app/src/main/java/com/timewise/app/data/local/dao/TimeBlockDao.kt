package com.timewise.app.data.local.dao

import androidx.room.*
import com.timewise.app.data.local.entity.TimeBlockEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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

    @Query("SELECT * FROM time_blocks WHERE taskId = :taskId")
    fun getByTask(taskId: Long): Flow<List<TimeBlockEntity>>

    // ---- Añadido para el Card #17 ----

    @Query("SELECT * FROM time_blocks WHERE date = :date ORDER BY startTime ASC")
    fun observeByDate(date: LocalDate): Flow<List<TimeBlockEntity>>

    @Query("SELECT * FROM time_blocks WHERE id = :id")
    suspend fun getById(id: Long): TimeBlockEntity?

    @Query("""
        SELECT * FROM time_blocks
        WHERE date = :date
          AND startTime < :end AND endTime > :start
          AND (:excludeId IS NULL OR id != :excludeId)
    """)
    suspend fun getOverlapping(
        date: LocalDate,
        start: Long,
        end: Long,
        excludeId: Long?
    ): List<TimeBlockEntity>

    // ---- Ya existente ----

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timeBlock: TimeBlockEntity): Long

    @Update
    suspend fun update(timeBlock: TimeBlockEntity)

    @Delete
    suspend fun delete(timeBlock: TimeBlockEntity)

    @Query("DELETE FROM time_blocks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query ("SELECT * FROM time_blocks WHERE date BETWEEN :startDate AND :endDate")
    fun observeByDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<TimeBlockEntity>>
}