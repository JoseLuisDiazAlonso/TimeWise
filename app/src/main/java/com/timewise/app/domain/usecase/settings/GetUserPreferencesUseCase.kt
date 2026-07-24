package com.timewise.app.domain.usecase.settings

import com.timewise.app.domain.model.UserPreferences
import com.timewise.app.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Esta clase controla las opciones que el usuario selecciona*
 * */

class GetUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() : Flow<UserPreferences> =
        userPreferencesRepository.observeUserPreferences()
}
