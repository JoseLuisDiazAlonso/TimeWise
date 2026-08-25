package com.timewise.app.widget.di

import com.timewise.app.domain.repository.WidgetUpdater
import com.timewise.app.widget.data.WidgetUpdaterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Enlaza la interfaz de dominio WidgetUpdater con su implementación real de infraestructura.
 * Al ser un @Binds, debe ser una clase abstracta
 * Métodos:
 *  - @Binds abstract fun bindWidgetUpdater(widgetUpdaterImpl: WidgetUpdaterImpl): WidgetUpdater
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class WidgetModule {
    @Binds
    abstract fun bindWidgetUpdater(widgetUpdaterImpl: WidgetUpdaterImpl): WidgetUpdater
}
