package com.timewise.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timewise.app.domain.model.TimeBlock

@Entity(tableName = "time_blocks")
data class TimeBlockEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long?,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val colorHex: String,
    val isRecurring: Boolean,
    val recurrenceRule: String?,
    val createdAt: Long
) {
    fun toDomain(): TimeBlock = TimeBlock(
        id = id,
        taskId = taskId,
        title = title,
        startTime = startTime,
        endTime = endTime,
        colorHex = colorHex,
        isRecurring = isRecurring,
        recurrenceRule = recurrenceRule,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(block: TimeBlock): TimeBlockEntity = TimeBlockEntity(
            id = block.id,
            taskId = block.taskId,
            title = block.title,
            startTime = block.startTime,
            endTime = block.endTime,
            colorHex = block.colorHex,
            isRecurring = block.isRecurring,
            recurrenceRule = block.recurrenceRule,
            createdAt = block.createdAt
        )
    }
}