package com.timewise.app.ui.settings

import android.content.Intent
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.data.local.locale.AppLocaleManager
import com.timewise.app.data.notifications.NotificationPermissionChecker
import com.timewise.app.domain.model.AppLanguage
import com.timewise.app.domain.usecase.settings.GetUserPreferencesUseCase
import com.timewise.app.domain.usecase.settings.SetLanguageUseCase
import com.timewise.app.domain.usecase.settings.SetNotificationsEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Esta clase va a configurar la vista de la pantalla de los settings.
 *  **/

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val setLanguageUseCase: SetLanguageUseCase,
    private val setNotificationsEnabledUseCase: SetNotificationsEnabledUseCase,
    private val appLocaleManager: AppLocaleManager,
    private val notificationPermissionChecker: NotificationPermissionChecker
): ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserPreferencesUseCase().collect { prefs ->
                _uiState.value = _uiState.value.copy(
                    language = prefs.language,
                    notificationsEnabled = prefs.notificationsEnabled,
                    isPremium = prefs.isPremium,
                    systemNotificationsBlocked =
                    !notificationPermissionChecker.areSystemNotificationsEnabled()
                )

            }
        }
    }

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            setLanguageUseCase(language)
            appLocaleManager.applylanguage(language)
        }
    }

    fun onNotificationsEnabled (enabled: Boolean) {
        viewModelScope.launch {
            setNotificationsEnabledUseCase(enabled)
        }
    }

    fun openSystemNotificationsSettings() : Intent =
        notificationPermissionChecker.openSystemNotificationsSettings()

    fun onNotificationsToggled(b: Boolean) {
            onNotificationsEnabled(b)
    }

    fun openSystemNotificationSettings(context: android.content.Context): Intent? {
        return Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
    }

}