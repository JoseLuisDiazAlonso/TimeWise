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
import com.timewise.app.ui.taskform.components.CategorySelector
import com.timewise.app.ui.taskform.components.DateTimeSection
import com.timewise.app.ui.taskform.components.PrioritySelector
import com.timewise.app.ui.taskform.components.TaskTitleField

/**Creamos una clase para ensamblar todos los componentes que hemos creado anteriormente
 * **/

@Composable
fun TaskFormScreen (
    viewModel: TaskFormViewModel = hiltViewModel(),
    onTaskSaved: @Composable () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onTaskSaved()
        }
        }
    Column (modifier = Modifier.padding(16.dp)) {
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


