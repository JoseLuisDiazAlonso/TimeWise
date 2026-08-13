package com.timewise.app.core.di

import com.timewise.app.data.local.dao.TimeBlockDao
import com.timewise.app.data.local.TimeWiseDatabase
import com.timewise.app.data.repository.TimeBlockRepositoryImpl
import com.timewise.app.domain.repository.TimeBlockRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TimeBlockingProvidesModule {

    @Provides
    fun provideTimeBlockDao(db: TimeWiseDatabase): TimeBlockDao {
        return db.timeBlockDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class TimeBlockingBindsModule {

    @Binds
    abstract fun bindTimeBlockRepository(
        impl: TimeBlockRepositoryImpl
    ): TimeBlockRepository
}