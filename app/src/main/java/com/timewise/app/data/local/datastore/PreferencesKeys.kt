package com.timewise.app.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 *Creamos un objeto en el cual se centralizan las claves de las preferencias de usuario y de
 * esta manera evitamos cadenas repetidas
 *
 * **/

object PreferencesKeys {
    val LANGUAGE_CODE = stringPreferencesKey("language_code")
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val IS_PREMIUM = booleanPreferencesKey("is_premium")
}
