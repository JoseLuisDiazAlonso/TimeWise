package com.timewise.app.domain.model

/*Esta clase representa un evento o cita con fecha y hora fija.
* A diferencia de Task (Tarea Pendiente) y TimeBlock (bloque visual de agenda),
* Event no tiene estado "completado, ni prioridad.**/

class Event (
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val location: String? = null,
    val startTime: Long,
    val endTime: Long,
    val isAllDay: Boolean = false,
    val reminderAt: Long? = null,
    val colorHex: String = "#03A9FA",
    val isRecurring: Boolean = false,
    val recurrenceRule: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updateAt: Long = System.currentTimeMillis()

)

