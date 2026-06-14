package com.timewise.app.core.di

import android.content.Context
import androidx.room.Room
import com.timewise.app.data.local.TimeWiseDatabase
import com.timewise.app.data.local.dao.TaskDao
import com.timewise.app.data.local.dao.TimeBlockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TimeWiseDatabase =
        Room.databaseBuilder(
            context,
            TimeWiseDatabase::class.java,
            "timewise.db"
        ).build()

    @Provides
    @Singleton
    fun provideTaskDao(database: TimeWiseDatabase): TaskDao =
        database.taskDao()

    @Provides
    @Singleton
    fun provideTimeBlockDao(database: TimeWiseDatabase): TimeBlockDao =
        database.timeBlockDao()
}