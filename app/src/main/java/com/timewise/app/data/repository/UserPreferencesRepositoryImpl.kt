package com.timewise.app.data.repository

import androidx.datastore.core.DataStore
import com.timewise.app.data.local.datastore.PreferencesKeys
import com.timewise.app.domain.model.AppLanguage
import com.timewise.app.domain.model.UserPreferences
import com.timewise.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject
import androidx.datastore.preferences.core.edit

/**
 * Esta clase controla las preferencias del usuario
 * Primero determinamos el modelo del dominio, después determinamos los valores por defecto que son:
 * español, notificaciones activadas y premium desactivado.
 * Por último lo que hacemos es controlar los cambios del valor que el usuario lleve a cabo en
 * esos ajustes
 * **/

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): UserPreferencesRepository {
    override suspend fun observeUserPreferences(): Flow<UserPreferences> =
        dataStore.data.map { prefs ->
            UserPreferences(
                language = AppLanguage.fromCode(
                    prefs[PreferencesKeys.LANGUAGE_CODE] ?: AppLanguage.SPANISH.languageCode
                ),
                notificationsEnabled = prefs[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true,
                isPremium = prefs[PreferencesKeys.IS_PREMIUM] ?: false
            )
        }

    override suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.LANGUAGE_CODE] = language.languageCode
        }
    }
    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }
    override suspend fun setPremium(isPremium: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_PREMIUM] = isPremium
        }
    }

}
