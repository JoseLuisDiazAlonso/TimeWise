package com.timewise.app.ui.onboarding

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.timewise.app.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

/*
Este archivo determina la selección del lenguaje por el usuario
*
*/

@Composable
fun LanguageSelectorPage () {
    var selected by remember { mutableStateOf (currentLanguageTag()) }

    Column (
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.onboarding_language_title), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.onboarding_language_desc), style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))

        LanguageOptionRow (
            tag = "es",
            label = "Espa/u00f1ol",
            selectedTag = selected,
        ) { tag ->
            selected = tag
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
        }
        Spacer(modifier = Modifier.height(16.dp))
        LanguageOptionRow (
            tag = "en",
            label = "English",
            selectedTag = selected,
        ) { tag ->
            selected = tag
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
        }
    }
}
@Composable
fun LanguageOptionRow(
    tag: String,
    label: String,
    selectedTag: String,
    content: (tag: String) -> Unit
) {
    val isSelected = tag == selectedTag
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { content(tag) }
            .background(backgroundColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = textColor, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun currentLanguageTag(): String {
    val locales = AppCompatDelegate.getApplicationLocales()
    return if (locales.isEmpty) {
        "es"
    } else {
        locales[0]?.language ?: "es"
    }
}