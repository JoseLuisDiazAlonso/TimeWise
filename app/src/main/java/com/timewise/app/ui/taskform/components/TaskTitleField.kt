package com.timewise.app.ui.taskform.components

/**
 * Creamos una función llamada TaskTitleField que lo que hace es capturar un texto y muestra
 * un error.
 * Este componente no guarda un estado propio si no que lo que hace es recibir un value
 * y notificar los cambios con onValueChange.*/

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.timewise.app.R

@Composable //Le indica al compilador que esta función solo describe una parte de la función
fun TaskTitleField (
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(stringResource(R.string.task_title)) },
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            Text(
                text = stringResource(R.string.error_title_empty),
                color = androidx.compose.material3.MaterialTheme.colorScheme.error
            )
        }

    }

}
