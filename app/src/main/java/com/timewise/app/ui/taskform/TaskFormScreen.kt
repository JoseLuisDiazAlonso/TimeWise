package com.timewise.app.ui.taskform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.timewise.app.R
import com.timewise.app.ui.common.ResponsiveScrollableScreen
import com.timewise.app.ui.taskform.components.CategorySelector
import com.timewise.app.ui.taskform.components.DateTimeSection
import com.timewise.app.ui.taskform.components.PrioritySelector
import com.timewise.app.ui.taskform.components.TaskDescriptionField
import com.timewise.app.ui.taskform.components.TaskTitleField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(
    viewModel: TaskFormViewModel = hiltViewModel(),
    onTaskSaved: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved, uiState.isDeleted) {
        if (uiState.isSaved || uiState.isDeleted) {
            onTaskSaved()
        }
    }

    val isEditing = uiState.id != null

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.task_delete_confirm_title)) },
            text = { Text(stringResource(R.string.task_delete_confirm_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                    viewModel.onDeleteClicked()
                }) {
                    Text(stringResource(R.string.task_delete_confirm_action))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditing) {
                            stringResource(R.string.task_edit_title)
                        } else {
                            stringResource(R.string.task_new_title)
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = { showDeleteConfirmation = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.task_delete_action))
                        }
                    }
                }
            )
        }
    ) { padding ->
        ResponsiveScrollableScreen(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TaskTitleField(
                    value = uiState.title,
                    isError = uiState.titleError,
                    onValueChange = { viewModel.onTitleChanged(it) }
                )
                TaskDescriptionField(
                    value = uiState.description,
                    onValueChange = { viewModel.onDescriptionChanged(it) }
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

                if (uiState.isSaving) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Button(
                    onClick = viewModel::onSavedClicked,
                    enabled = !uiState.isSaving,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.btn_save))
                }
            }
        }
    }
}