package com.timewise.app.data.preferences

import android.content.Context
import com.timewise.app.domain.repository.AdFrequencyRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Esta clase se utiliza para no bloquear el hilo principal. Igual que haría otra operación de
 * E/S en la app
 *
 * **/

class AdFrequencyPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AdFrequencyRepository {
    private val preferences = context.getSharedPreferences("ad_frequency", Context.MODE_PRIVATE)

    override suspend fun getLastShownTimestamp(): Long {
        return preferences.getLong("last_shown_timestamp", 0L)
    }

    override suspend fun updateLastShownTimestamp(timestamp: Long) {
        preferences.edit().putLong("last_shown_timestamp", timestamp).apply()
    }
}