package com.timewise.app.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.timewise.app.widget.ui.WidgetContent

import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.first

class TimeWiseWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        val getWidgetDataUseCase = entryPoint.getWidgetDataUseCase()

        // Lectura puntual: no queremos mantener una suscripción infinita aquí,
        // solo el estado actual en el momento de pintar (mismo principio que
        // ya aplicaste con .first() en otras partes).
        val uiState = getWidgetDataUseCase()

        provideContent {
            WidgetContent(state = uiState)
        }
    }
}
