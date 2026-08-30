package com.timewise.app.ui.taskform

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.timewise.app.R
import com.timewise.app.ui.common.ResponsiveScrollableScreen
import com.timewise.app.ui.taskform.components.CategorySelector
import com.timewise.app.ui.taskform.components.DateTimeSection
import com.timewise.app.ui.taskform.components.PrioritySelector
import com.timewise.app.ui.taskform.components.TaskTitleField

/**
 * Ensambla todos los componentes del formulario de tarea.
 */
@Composable
fun TaskFormScreen(
    viewModel: TaskFormViewModel = hiltViewModel(),
    onTaskSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onTaskSaved()
        }
    }

    // ResponsiveScrollableScreen sustituye al Column suelto de nivel superior:
    // añade scroll (necesario aquí porque con varios campos + selectores es fácil
    // que no quepa en landscape) y centra el contenido con ancho máximo en tablet.
    ResponsiveScrollableScreen {
        Column(modifier = Modifier.padding(16.dp)) {
            TaskTitleField(
                value = uiState.title,
                isError = uiState.titleError,
                onValueChange = { viewModel.onTitleChanged(it) }
            )
            DateTimeSection(
                date = uiState.dueDate,
                time = uiState.dueTime,
                onDateSelected = { viewModel.onDueDateChanged(it) },
                onTimeSelected = { viewModel.onDueTimeChanged(it) }
            )
            PrioritySelector(
                selected = uiState.priority,
                onPrioritySelected = { viewModel.onPriorityChanged(it) }
            )
            CategorySelector(
                selected = uiState.categoryOption,
                onCategorySelected = { viewModel.onCategoryChanged(it) }
            )
            Button(
                onClick = viewModel::onSavedClicked,
                enabled = !uiState.isSaving
            ) {
                Text(text = stringResource(R.string.btn_save))
            }
        }
    }
}