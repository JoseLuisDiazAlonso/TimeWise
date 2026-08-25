package com.timewise.app.widget.di

import com.timewise.app.domain.repository.WidgetUpdater
import com.timewise.app.domain.usecase.widget.GetWidgetDataUseCase

/**
 * Es el punto de entrada manual a Hilt para acceder a dependencias del grafo de inyección
 * desde clases Hilt no instancias directamente, como GlanceAppWidget. Se accede mediante
 * EntryPointAccesors.fromApplication (context, WidgetEntryPoint::class.java
 * Métodos
 *  - fun getWidgetDataUseCase(): GetWidgetDataUseCase
 *  - fun widgetUpdater(): WidgetUpdater
 */

interface WidgetEntryPoint {
    fun getWidgetDataUseCase(): GetWidgetDataUseCase
    fun widgetUpdater(): WidgetUpdater
}