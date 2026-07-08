package com.timewise.app.core.di
/**Creamos una clase que me permita que cuando el ViewModel le pida a Hilt que muestre
 * una interfaz (TaskRepository) la inyecte con las dependencias necesarias. (muestre el código real**/

import com.timewise.app.data.repository.EventRepositoryImpl
import com.timewise.app.data.repository.TaskRepositoryImpl
import com.timewise.app.data.repository.TimeBlockRepositoryImpl
import com.timewise.app.domain.repository.EventRepository
import com.timewise.app.domain.repository.TaskRepository
import com.timewise.app.domain.repository.TimeBlockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindTimeBlockRepository(
        impl: TimeBlockRepositoryImpl
    ): TimeBlockRepository

    @Binds
    @Singleton
    abstract fun bindEventRepository(
        impl: EventRepositoryImpl
    ): EventRepository
}