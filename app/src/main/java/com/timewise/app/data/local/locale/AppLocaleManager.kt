package com.timewise.app.data.local.locale

import android.content.Context
import com.timewise.app.domain.model.AppLanguage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Esta clase controla el idioma que selecciona el usuario y lo persiste
 * de forma síncrona, para que LocaleHelper.wrap() pueda leerlo de inmediato.
 */
class AppLocaleManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun applylanguage(language: AppLanguage) {
        LocaleHelper.persist(context, language.languageCode)
    }
}