package com.timewise.app.ui.timeblocking

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import com.timewise.app.ui.taskform.components.CategorySelector
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeBlockFormBottomSheet(
    initialBlock: TimeBlockUiModel?,
    onDismiss: () -> Unit,
    onConfirm: (title: String, start: LocalTime, end: LocalTime, colorHex: String) -> Unit,
    onDelete: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    // Los bloques que vienen de una tarea (taskId != null) solo permiten ajustar la
    // hora: el título y la categoría se heredan de la tarea y se editan desde ahí.
    val isFromTask = initialBlock?.taskId != null

    var title by remember { mutableStateOf(initialBlock?.title ?: "") }
    var titleError by remember { mutableStateOf(false) }
    var startTime by remember {
        mutableStateOf(
            initialBlock?.let { minutesToLocalTime(it.offsetMinutes) } ?: LocalTime.of(9, 0)
        )
    }
    var endTime by remember {
        mutableStateOf(
            initialBlock?.let { minutesToLocalTime(it.offsetMinutes + it.durationMinutes) }
                ?: LocalTime.of(9, 30)
        )
    }
    var category by remember {
        mutableStateOf(
            initialBlock?.let { categoryOptionForHex(it.categoryColor) }
                ?: com.timewise.app.ui.taskform.availableCategories.first()
        )
    }

    val isEditing = initialBlock != null

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(
                    if (isEditing) R.string.time_blocking_edit_block
                    else R.string.time_blocking_add_block
                ),
                style = MaterialTheme.typography.titleLarge
            )

            if (isFromTask) {
                // Título fijo, no editable: viene de la tarea vinculada.
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = stringResource(R.string.time_blocking_from_task_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; titleError = false },
                    isError = titleError,
                    label = { Text(stringResource(R.string.task_title_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute -> startTime = LocalTime.of(hour, minute) },
                        startTime.hour, startTime.minute, true
                    ).show()
                }) {
                    Text("${stringResource(R.string.time_blocking_start_time)}: ${startTime.format(timeFormatter)}")
                }
                OutlinedButton(onClick = {
                    TimePickerDialog(
                        context,
                        { _, hour, minute -> endTime = LocalTime.of(hour, minute) },
                        endTime.hour, endTime.minute, true
                    ).show()
                }) {
                    Text("${stringResource(R.string.time_blocking_end_time)}: ${endTime.format(timeFormatter)}")
                }
            }

            if (!isFromTask) {
                Text(stringResource(R.string.time_blocking_category), style = MaterialTheme.typography.labelMedium)
                CategorySelector(
                    selected = category,
                    onCategorySelected = { category = it }
                )
            }

            Button(
                onClick = {
                    if (!isFromTask && title.isBlank()) {
                        titleError = true
                        return@Button
                    }
                    val finalColorHex = if (isFromTask) {
                        initialBlock?.categoryColor ?: category.toHexString()
                    } else {
                        category.toHexString()
                    }
                    onConfirm(title, startTime, endTime, finalColorHex)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_save))
            }

            if (isEditing && !isFromTask) {
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(R.string.time_blocking_delete_block),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}