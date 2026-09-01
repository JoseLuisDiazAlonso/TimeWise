package com.timewise.app.ui.onboarding

import androidx.lifecycle.ViewModel
import com.timewise.app.data.local.locale.AppLocaleManager
import com.timewise.app.domain.model.AppLanguage
import com.timewise.app.domain.usecase.settings.SetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageSelectorViewModel @Inject constructor(
    private val setLanguageUseCase: SetLanguageUseCase,
    private val appLocaleManager: AppLocaleManager
) : ViewModel() {
    suspend fun selectLanguage(language: AppLanguage) {
        setLanguageUseCase(language)
        appLocaleManager.applylanguage(language)
    }
}