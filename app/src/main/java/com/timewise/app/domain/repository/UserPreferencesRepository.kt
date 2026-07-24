package com.timewise.app.domain.repository

import com.timewise.app.domain.model.AppLanguage
import com.timewise.app.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Esta interface hace muestra las tres funciones que controlan las opciones que deberá de
 * seleccionar el usuario
 * **/

interface UserPreferencesRepository {
    suspend fun observeUserPreferences() : Flow<UserPreferences>
    suspend fun setLanguage (language: AppLanguage)
    suspend fun setNotificationsEnabled (enabled: Boolean)
    suspend fun setPremium (isPremium: Boolean)
}