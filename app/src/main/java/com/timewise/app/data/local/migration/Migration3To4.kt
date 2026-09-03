package com.timewise.app.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migración de la versión 3 a la 4: añade reminderEnabled a "tasks" para poder
 * activar/desactivar un recordatorio individualmente sin perder la hora
 * guardada en reminderAt. Por defecto true, para no desactivar silenciosamente
 * los recordatorios que ya tuvieran los usuarios existentes.
 */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE tasks ADD COLUMN reminderEnabled INTEGER NOT NULL DEFAULT 1")
    }
}