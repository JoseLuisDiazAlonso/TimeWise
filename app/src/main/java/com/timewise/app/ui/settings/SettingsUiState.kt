package com.timewise.app.ui.settings

import com.timewise.app.domain.model.AppLanguage

/**
 * Determina las variables de los settings siendo estos: val language que es Spanish por
 * defecto. notificationsEnables que es Boolean y true por defecto, systemNotificationsBlocked que
 * es Boolean y false por defecto, isPremium que es Boolean y false por defecto.
 * **/

data class SettingsUiState(
    val language: AppLanguage = AppLanguage.SPANISH,
    val notificationsEnabled: Boolean = true,
    val systemNotificationsBlocked: Boolean = false,
    val isPremium: Boolean = false
)
