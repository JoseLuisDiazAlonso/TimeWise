package com.timewise.app.domain.usecase.settings

import com.timewise.app.domain.model.AppLanguage
import com.timewise.app.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Esta clase controla la selección de idioma del usuario
 * **/

/**
 * El constructor es private val userPreferencesRepository: UserPreferencesRepository
 * La función que utiliza es suspend operator fun invoke(language: AppLanguage)**/
class SetLanguageUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(language: AppLanguage) {
        userPreferencesRepository.setLanguage(language)
    }
}
