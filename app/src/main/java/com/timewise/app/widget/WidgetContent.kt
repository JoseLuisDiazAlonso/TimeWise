package com.timewise.app.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.timewise.app.domain.model.widget.TaskSummary
import com.timewise.app.domain.model.widget.WidgetUiState

/**
 * Composables Glance puros, sin lógica de negocio. Reciben un WidgetUiState ya resuelto y lo
 * pintan. Se separan de TimeWiseWidget para poder iterar el diseño visual sin tocar la
 * obtención de datos.
 */

@Composable
fun WidgetContent(state: WidgetUiState) {
    when {
        !state.isUnlocked -> LockedWidgetContent()
        state.todayTask.isEmpty() && state.nextReminder == null -> EmptyWidgetContent()
        else -> {
            Column(modifier = GlanceModifier.fillMaxSize().padding(12.dp)) {
                state.nextReminder?.let {
                    Text(text = it.title)
                }
                state.todayTask.forEach { task ->
                    TaskRow(taskSummary = task)
                }
            }
        }
    }
}

@Composable
fun LockedWidgetContent() {
    Column(modifier = GlanceModifier.fillMaxSize().padding(12.dp)) {
        Text(text = "Función Premium")
    }
}

@Composable
fun EmptyWidgetContent() {
    Column(modifier = GlanceModifier.fillMaxSize().padding(12.dp)) {
        Text(text = "Sin tareas pendientes")
    }
}

@Composable
fun TaskRow(taskSummary: TaskSummary) {
    Text(text = taskSummary.title)
}