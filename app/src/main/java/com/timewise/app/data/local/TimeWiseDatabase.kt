package com.timewise.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timewise.app.data.local.dao.TaskDao
import com.timewise.app.data.local.dao.TimeBlockDao
import com.timewise.app.data.local.entity.TaskEntity
import com.timewise.app.data.local.entity.TimeBlockEntity

@Database (
    entities = [TaskEntity::class, TimeBlockEntity::class],
    version = 1,
    exportSchema = true
)

abstract class TimeWiseDatabase : RoomDatabase () {
    abstract fun taskDao(): TaskDao
    abstract fun timeBlockDao(): TimeBlockDao
}
