package com.timewise.app.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 * Es el puente entre el sistema Android (AppWidgetManager) y la implementación de Glance.
 * Es la clase que se declara como <receiver> en el AndroidManifest. Su única responsabilidad
 * es exponer la instancia de GlanceAppWidget a usar.
 */
class TimeWiseWidgetReceiver : GlanceAppWidgetReceiver() {
 override val glanceAppWidget: GlanceAppWidget = TimeWiseWidget()
}