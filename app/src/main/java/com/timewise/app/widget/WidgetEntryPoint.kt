package com.timewise.app.widget

import com.timewise.app.domain.usecase.widget.GetWidgetDataUseCase

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Punto de entrada manual a Hilt para clases que Android instancia por sí mismo
 * (no vía constructor), como GlanceAppWidget. Expone el caso de uso que
 * TimeWiseWidget necesita para obtener los datos.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getWidgetDataUseCase(): GetWidgetDataUseCase
}