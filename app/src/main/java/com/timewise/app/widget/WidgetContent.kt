package com.timewise.app.widget.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.timewise.app.domain.model.widget.TaskSummary
import com.timewise.app.domain.model.widget.WidgetUiState
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Composables Glance puros, sin lógica de negocio. Reciben un WidgetUiState ya resuelto y lo
 * pintan. Se separan de TimeWiseWidget para poder iterar el diseño visual sin tocar la
 * obtención de datos.
 */

private val WidgetBackground = Color(0xFF1E1E2E)
private val WidgetTextPrimary = Color(0xFFFFFFFF)
private val WidgetTextSecondary = Color(0xFFB0B0C0)
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@Composable
fun WidgetContent(state: WidgetUiState) {
    when {
        !state.isUnlocked -> LockedWidgetContent()
        state.todayTask.isEmpty() && state.nextReminder == null -> EmptyWidgetContent()
        else -> {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ColorProvider(WidgetBackground))
                    .padding(12.dp)
            ) {
                state.nextReminder?.let {
                    Text(
                        text = it.title,
                        style = TextStyle(
                            color = ColorProvider(WidgetTextPrimary),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )
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
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(WidgetBackground))
            .padding(12.dp)
    ) {
        Text(
            text = "Función Premium",
            style = TextStyle(color = ColorProvider(WidgetTextPrimary))
        )
    }
}

@Composable
fun EmptyWidgetContent() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(WidgetBackground))
            .padding(12.dp)
    ) {
        Text(
            text = "Sin tareas pendientes",
            style = TextStyle(color = ColorProvider(WidgetTextPrimary))
        )
    }
}

@Composable
fun TaskRow(taskSummary: TaskSummary) {
    Row(modifier = GlanceModifier.fillMaxWidth().padding(vertical = 2.dp)) {
        taskSummary.time?.let { time ->
            Text(
                text = time.format(timeFormatter),
                style = TextStyle(
                    color = ColorProvider(WidgetTextSecondary),
                    fontSize = 12.sp
                ),
                modifier = GlanceModifier.padding(end = 8.dp)
            )
        }
        Text(
            text = taskSummary.title,
            style = TextStyle(
                color = ColorProvider(WidgetTextPrimary),
                fontSize = 14.sp
            )
        )
    }
}