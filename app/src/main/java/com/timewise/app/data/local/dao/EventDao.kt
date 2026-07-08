package com.timewise.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.timewise.app.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY startTime ASC")
    fun getAll(): Flow<List<EventEntity>>

    @Query ("SELECT * FROM events WHERE id = :id")
    fun getById (id: Long): Flow<EventEntity?>

    @Query ("""
        SELECT * FROM events
        WHERE startTime >= :startTime AND endTime <= :endTime
        ORDER BY startTime ASC
    """)
    fun getByDateRange (startTime: Long, endTime: Long): Flow<List<EventEntity>>

    @Query ("SELECT * FROM events WHERE startTime >= :fromTime ORDER BY startTime ASC")
    fun getUpcoming (fromTime: Long = System.currentTimeMillis()): Flow<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (event: EventEntity): Long

    @Update
    suspend fun update (event: EventEntity)

    @Delete
    suspend fun delete (event: EventEntity)

    @Query ("DELETE FROM events WHERE id = :id")
    suspend fun deleteById (id: Long)
}
