package com.timewise.app.data.local.locale


import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.timewise.app.domain.model.AppLanguage
import javax.inject.Inject

/**
 * Esta clase lo que hará es controlar el idioma que seleccione el usuario y mostrarlo.
 *
 * **/

class AppLocaleManager @Inject constructor(){
    fun applylanguage(language: AppLanguage) {
        val localeList = LocaleListCompat.forLanguageTags(language.languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)

    }
}