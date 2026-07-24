package com.timewise.app.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timewise.app.R
import com.timewise.app.domain.model.AppLanguage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPremium: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            SettingsSectionHeader(stringResource(R.string.settings_section_language))
            AppLanguage.entries.forEach { language ->
                LanguageOptionRow(
                    language = language,
                    selected = uiState.language == language,
                    onClick = { viewModel.onLanguageSelected(language) }
                )
            }

            HorizontalDivider()
            SettingsSectionHeader(stringResource(R.string.settings_section_notifications))
            SettingsSwitchRow(
                title = stringResource(R.string.settings_notifications_enabled),
                checked = uiState.notificationsEnabled,
                onCheckedChange = viewModel::onNotificationsToggled
            )
            if (uiState.systemNotificationsBlocked) {
                TextButton(onClick = {
                    context.startActivity(viewModel.openSystemNotificationSettings(context))
                }) {
                    Text(stringResource(R.string.settings_notifications_blocked_action))
                }
            }

            HorizontalDivider()
            SettingsSectionHeader(stringResource(R.string.settings_section_premium))
            PremiumStatusCard(
                isPremium = uiState.isPremium,
                onClick = onNavigateToPremium
            )
        }
    }
}