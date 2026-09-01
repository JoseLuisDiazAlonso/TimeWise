package com.timewise.app.data.local.locale

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Sistema de idioma propio, independiente de AppCompatDelegate/LocaleManager.
 * Necesario porque en ciertas ROMs (confirmado en TCL) el mecanismo estándar de
 * Android no aplica el idioma aunque se invoque correctamente y sin errores.
 */
object LocaleHelper {
    private const val PREFS_NAME = "locale_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    /** Guarda el idioma de forma síncrona (SharedPreferences, no DataStore),
     * para poder leerlo en attachBaseContext antes de que arranque nada más. */
    fun persist(context: Context, languageCode: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageCode)
            .apply()
    }

    fun getPersistedLanguage(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, null)
    }

    /** Envuelve el Context con el idioma guardado, forzando qué recursos se cargan
     * sin depender de que el sistema operativo lo haga por su cuenta. */
    fun wrap(context: Context): Context {
        val languageCode = getPersistedLanguage(context) ?: return context
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}