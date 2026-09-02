package com.timewise.app.ui.taskform.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.timewise.app.R

@Composable
fun TaskDescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.task_description_label)) },
        placeholder = { Text(stringResource(R.string.task_description_hint)) },
        minLines = 3,
        maxLines = 5,
        modifier = modifier.fillMaxWidth()
    )
}