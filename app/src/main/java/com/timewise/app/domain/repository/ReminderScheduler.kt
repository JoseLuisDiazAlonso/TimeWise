package com.timewise.app.domain.repository

/**
 * Creamos una interface que nos permita programar un recordatorio recibiendo los datos necesarios
 * Además contiene un método para cancelar el recordatorio
 * **/

interface ReminderScheduler {
    fun schedule (reminderId: Long, title: String, triggerAtMillis: Long)
    fun cancel (reminderId: Long)
    abstract fun schedule(reminderId: Long, title: String)

    companion object {
        fun schedule(reminderId: Long, title: String) {

        }
    }
}
