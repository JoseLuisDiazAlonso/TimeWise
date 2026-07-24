package com.timewise.app.domain.usecase.settings

import com.timewise.app.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Esta clase lo que va a controlar es la selección de las notificaciones del usuario
 ***/

class SetNotificationsEnabledUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        userPreferencesRepository.setNotificationsEnabled(enabled)
    }
}
