package com.timewise.app.data.preferences

import android.content.Context
import com.timewise.app.domain.repository.TemporaryUnlockRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementa TemporaryUnlockRepository con SharedPreferences, reutilizando el mismo fichero
 * ads_prefs creado en el Card #13 del proyecto. El objetivo es no fragmentar el almacenamiento
 * de datos de ads en varios ficheros.
 */
class TemporaryUnlockPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TemporaryUnlockRepository {

    private val prefs by lazy {
        context.getSharedPreferences("ads_prefs", Context.MODE_PRIVATE)
    }

    override suspend fun getUnlockExpirationTime(): Long = withContext(Dispatchers.IO) {
        prefs.getLong(KEY_UNLOCK_EXPIRATION, 0L)
    }

    override suspend fun grantTemporaryUnlock(durationMillis: Long) {
        withContext(Dispatchers.IO) {
            val expiration = System.currentTimeMillis() + durationMillis
            prefs.edit().putLong(KEY_UNLOCK_EXPIRATION, expiration).apply()
        }
    }

    override suspend fun isUnlockActive(): Boolean {
        return getUnlockExpirationTime() > System.currentTimeMillis()
    }

    private companion object {
        const val KEY_UNLOCK_EXPIRATION = "unlock_expiration"
    }
}