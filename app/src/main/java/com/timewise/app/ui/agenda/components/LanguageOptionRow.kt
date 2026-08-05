package com.timewise.app.ui.agenda.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import com.timewise.app.domain.model.AppLanguage

/**
 * Este archivo determinará la fila para la selección de idioma
 * **/

@Composable
private fun LanguageOptionRow(
    language: AppLanguage,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16 .dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = when (language) {
                AppLanguage.SPANISH -> stringResource(R.string.language_spanish)
                AppLanguage.ENGLISH -> stringResource(R.string.language_english)
            },
            style = MaterialTheme.typography.bodyLarge
        )

    }

}
