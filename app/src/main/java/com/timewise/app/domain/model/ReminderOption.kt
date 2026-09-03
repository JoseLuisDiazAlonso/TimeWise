package com.timewise.app.domain.model

/**
 * Representa cuánto tiempo antes de la fecha límite de la tarea debe saltar
 * el recordatorio. null en offsetMinutes significa "sin recordatorio".
 */
enum class ReminderOption(val offsetMinutes: Long?) {
    NONE(null),
    AT_TIME(0),
    MIN_5(5),
    MIN_10(10),
    MIN_15(15),
    MIN_30(30),
    HOUR_1(60),
    DAY_1(1440)
}