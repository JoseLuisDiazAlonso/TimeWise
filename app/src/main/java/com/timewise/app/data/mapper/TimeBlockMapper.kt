package com.timewise.app.data.mapper

import com.timewise.app.data.local.entity.TimeBlockEntity
import com.timewise.app.domain.model.TimeBlock

/**
 * Es una función de extensión que convierten entre la entidad Room y el modelo de dominio,
 * siguiendo el mismo patrón que los mappers de Tareas y Agenda del M1.
 *
 * Funciones:
 *  - fun TimeBlockEntitiy.toDomain (): TimeBlock. Parsea date con LocalDate.parse (date) y
 *  startTime/endTime con LocalTime.parse (....)
 *  - fun TimeBlock.toEntity (): TimeBlockEntity. convierte los campos LocalDate/LocalTime a
 *  String con toString.
 *  **/

object TimeBlockMapper {
    fun TimeBlockEntity.toDomain(): TimeBlock {
        return TimeBlock(
            id = this.id,
            taskId = this.taskId,
            title = this.title,
            date = this.date,
            startTime = this.startTime,
            endTime = this.endTime,
            colorHex = this.colorHex,
            isRecurring = this.isRecurring,
            recurrenceRule = this.recurrenceRule,
            isCompleted = this.isCompleted,
            createdAt = this.createdAt
        )
    }
}