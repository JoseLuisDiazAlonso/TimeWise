package com.timewise.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timewise.app.domain.model.TimeBlock
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "time_blocks")
data class TimeBlockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long?,
    val title: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val colorHex: String,
    val isRecurring: Boolean,
    val recurrenceRule: String?,
    val isCompleted: Boolean,
    val createdAt: Long
) {
    fun toDomain(): TimeBlock = TimeBlock(
        id = id,
        taskId = taskId,
        title = title,
        date = date,
        startTime = startTime,
        endTime = endTime,
        colorHex = colorHex,
        isRecurring = isRecurring,
        recurrenceRule = recurrenceRule,
        isCompleted = isCompleted,
        createdAt = createdAt,
        updatedAt = System.currentTimeMillis()
    )

    companion object {
        fun fromDomain(block: TimeBlock): TimeBlockEntity = TimeBlockEntity(
            id = block.id,
            taskId = block.taskId,
            title = block.title,
            date = block.date,
            startTime = block.startTime,
            endTime = block.endTime,
            colorHex = block.colorHex,
            isRecurring = block.isRecurring,
            recurrenceRule = block.recurrenceRule,
            isCompleted = block.isCompleted,
            createdAt = block.createdAt
        )
    }
}