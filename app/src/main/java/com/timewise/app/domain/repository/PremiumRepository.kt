package com.timewise.app.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Esta interface es la que determinará si el usuario tiene acceso a las funciones premium.
 * **/

interface PremiumRepository {
    abstract val KEY_IS_PREMIUM: String?

    fun observeisPremium(): Flow<Boolean>
    abstract fun persistPremiumFlag(premium: Boolean)
    abstract fun readPersistedFlag(): Boolean
}