package com.timewise.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val categoryId: Long?,
    val isCompleted: Boolean,
    val priority: String,
    val dueDate: Long?,
    val reminderAt: Long?,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomain(): Task = Task(
        id = id,
        title = title,
        description = description,
        categoryId = categoryId,
        isCompleted = isCompleted,
        priority = Priority.valueOf(priority),
        dueDate = dueDate,
        reminderAt = reminderAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(task: Task): TaskEntity = TaskEntity(
            id = task.id,
            title = task.title,
            description = task.description,
            categoryId = task.categoryId,
            isCompleted = task.isCompleted,
            priority = task.priority.name,
            dueDate = task.dueDate,
            reminderAt = task.reminderAt,
            createdAt = task.createdAt,
            updatedAt = task.updatedAt
        )
    }
}