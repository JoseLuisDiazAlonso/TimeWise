package com.timewise.app.data.repository

import android.content.Context
import com.timewise.app.domain.model.PurchaseState
import com.timewise.app.domain.repository.BillingRepository
import com.timewise.app.domain.repository.PremiumRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Permite que el estado premium siga disponible aunque la app arranque
 * sin conexión, usando el último valor verificado y persistido.
 */
class PremiumRepositoryImpl @Inject constructor(
    private val billingRepository: BillingRepository,
    @ApplicationContext private val context: Context
) : PremiumRepository {

    private val prefs by lazy {
        context.getSharedPreferences("premium_prefs", Context.MODE_PRIVATE)
    }

    override fun observeIsPremium(): Flow<Boolean> =
        billingRepository.observePurchaseState()
            .map { state -> state == PurchaseState.Purchased }
            .onEach { isPremium -> persistPremiumFlag(isPremium) }
            .onStart { emit(readPersistedFlag()) }

    override fun readPersistedFlag(): Boolean =
        prefs.getBoolean(KEY_IS_PREMIUM, false)

    override fun persistPremiumFlag(value: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PREMIUM, value).apply()
    }

    private companion object {
        const val KEY_IS_PREMIUM = "key_is_premium"
    }
}

