package com.timewise.app.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Determina si el usuario tiene acceso a las funciones premium.
 */
interface PremiumRepository {
    fun observeIsPremium(): Flow<Boolean>
    fun persistPremiumFlag(premium: Boolean)
    fun readPersistedFlag(): Boolean
}