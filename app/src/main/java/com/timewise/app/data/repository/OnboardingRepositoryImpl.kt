package com.timewise.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.timewise.app.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/*
Esta clase lo que hace es configurar la selección de preferencias del usuario.*
*
*/

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) :OnboardingRepository {
    private object Keys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    override fun isOnboardingCompleted() = dataStore.data.map { preferences ->
        preferences[Keys.ONBOARDING_COMPLETED] ?: false
    }

    override suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = true
        }
    }

}