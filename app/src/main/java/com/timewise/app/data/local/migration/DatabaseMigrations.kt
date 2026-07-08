package com.timewise.app.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migración de la versión 1 a la versión 2 de la base de datos.
 * Añade la tabla "events" sin tocar "tasks" ni "time_blocks", por lo que
 * los datos que el usuario ya tenga guardados se conservan intactos.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS events (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                location TEXT,
                startTime INTEGER NOT NULL,
                endTime INTEGER NOT NULL,
                isAllDay INTEGER NOT NULL,
                reminderAt INTEGER,
                colorHex TEXT NOT NULL,
                isRecurring INTEGER NOT NULL,
                recurrenceRule TEXT,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}