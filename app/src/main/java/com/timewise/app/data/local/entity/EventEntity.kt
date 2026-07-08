package com.timewise.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timewise.app.domain.model.Event

@Entity(tableName = "events")
data class EventEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val location: String?,
    val startTime: Long,
    val endTime: Long,
    val isAllDay: Boolean,
    val reminderAt: Long?,
    val colorHex: String,
    val isRecurring: Boolean,
    val recurrenceRule: String?,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomain(): Event = Event(
        id = id,
        title = title,
        description = description,
        location = location,
        startTime = startTime,
        endTime = endTime,
        isAllDay = isAllDay,
        reminderAt = reminderAt,
        colorHex = colorHex,
        isRecurring = isRecurring,
        recurrenceRule = recurrenceRule,
        createdAt = createdAt,
        updateAt = updatedAt
    )
    companion object {
        fun fromDomain(event: Event): EventEntity = EventEntity(
            id = event.id,
            title = event.title,
            description = event.description,
            location = event.location,
            startTime = event.startTime,
            endTime = event.endTime,
            isAllDay = event.isAllDay,
            reminderAt = event.reminderAt,
            colorHex = event.colorHex,
            isRecurring = event.isRecurring,
            recurrenceRule = event.recurrenceRule,
            createdAt = event.createdAt,
            updatedAt = event.updateAt
        )
    }
}

