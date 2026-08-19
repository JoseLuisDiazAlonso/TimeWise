package com.timewise.app.data.local

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class Converters {

    @TypeConverter
    fun fromEpochDay(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun localDateToEpochDay(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun fromSecondOfDay(secondOfDay: Long?): LocalTime? {
        return secondOfDay?.let { LocalTime.ofSecondOfDay(it) }
    }

    @TypeConverter
    fun localTimeToSecondOfDay(time: LocalTime?): Long? {
        return time?.toSecondOfDay()?.toLong()
    }
}