package com.timewise.app.ui.taskform.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Row

import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.timewise.app.R
import java.time.LocalDate
import java.time.LocalTime

/**
 * Esta clase va a mostrar de forma legible la fecha seleccionada
 * **/


@Composable
fun DateTimeSection (
    date: LocalDate?,
    time: LocalTime?,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current //captura el contexto de la aplicación para poder usarlo en los diálogos de fecha y hora

    Row (modifier = modifier) {
        OutlinedButton(onClick = {
            val today = LocalDate.now()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    onDateSelected(
                        LocalDate.of(
                            year,
                            month + 1,
                            dayOfMonth
                        )
                    )
                },
                today.year, today.monthValue - 1, today.dayOfMonth
            ).show()
        }) {
            Text(text =date?.toString() ?: stringResource(id = R.string.task_due_date))
        }
        OutlinedButton(onClick = {
            val now = LocalTime.now()
            TimePickerDialog (
                context,
                { _, hour, minute -> onTimeSelected(LocalTime.of(hour, minute)) },
                now.hour, now.minute, true
            ).show(
            )
        }) {
            Text(text = date?.toString() ?: stringResource(id = R.string.cd_time_icon))
        }
    }
}