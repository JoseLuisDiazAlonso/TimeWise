package com.timewise.app.ui.taskform.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Muestra la fecha y hora seleccionadas en un formato legible, y abre los
 * diálogos nativos de Android para elegir cada una.
 * **/

private val dateFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

@Composable
fun DateTimeSection(
    date: LocalDate?,
    time: LocalTime?,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(onClick = {
            val base = date ?: LocalDate.now()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
                },
                base.year, base.monthValue - 1, base.dayOfMonth
            ).show()
        }) {
            Icon(Icons.Filled.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(6.dp))
            Text(text = date?.format(dateFormatter) ?: stringResource(id = R.string.task_due_date))
        }

        OutlinedButton(onClick = {
            val base = time ?: LocalTime.now()
            TimePickerDialog(
                context,
                { _, hour, minute -> onTimeSelected(LocalTime.of(hour, minute)) },
                base.hour, base.minute, true
            ).show()
        }) {
            Icon(Icons.Filled.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(6.dp))
            // Antes decía "date?.toString()" por error -> mostraba la fecha en el botón de la hora.
            Text(text = time?.format(timeFormatter) ?: stringResource(id = R.string.cd_time_icon))
        }
    }
}