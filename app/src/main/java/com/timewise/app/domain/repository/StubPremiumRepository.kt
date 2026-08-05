package com.timewise.app.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Esta clase devuelve false sistematicamente, de tal manera que nos indica que el usuario no
 * tiene acceso a las funciones premium
 *
 * Es un constructor que llama al PremiumRepository y sobreescribe el fun observeIsPremium del
 * PremiumRepository para devolver false.
 * **/

abstract class StubPremiumRepository @Inject constructor() : PremiumRepository {
    override val KEY_IS_PREMIUM: String? = null
    override fun observeisPremium(): Flow<Boolean> {
        return flowOf(false)
    }
}