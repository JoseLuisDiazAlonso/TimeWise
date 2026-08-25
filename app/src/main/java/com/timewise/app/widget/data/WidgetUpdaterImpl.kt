package com.timewise.app.widget.data

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.timewise.app.domain.repository.WidgetUpdater
import com.timewise.app.widget.TimeWiseWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Implementación real del puerto de dominio. Usa GlanceAppWidgetManager para forzar el
 * redibujado de todas las instancias del widget activas en el dispositivo.
 *
 * Dependencias:
 *  - @ApplicationContext context: Context
 * Métodos:
 *  - override suspend fun refreshWidget(): Unit**/

class WidgetUpdaterImpl @Inject constructor(
    @ApplicationContext private val context: Context
): WidgetUpdater {
    override suspend fun refreshWidget(): Unit {
        GlanceAppWidgetManager(context).getGlanceIds(TimeWiseWidget::class.java).forEach {
            TimeWiseWidget().update(context, it)
        }
    }

}
