package com.timewise.app.domain.model

/*Task representa una tarea en la aplicación**/

enum class Priority {LOW, MEDIUM, HIGH}

data class Task (
    val id: Long = 0,
    val title: String,
    val description: String ="",
    val categoryId: Long? = null,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val reminderAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

