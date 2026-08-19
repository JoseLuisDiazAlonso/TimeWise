package com.timewise.app.domain.usecase.premium

import com.timewise.app.domain.repository.PremiumRepository
import javax.inject.Inject

/**
 * Verificación puntual (one-shot) del estado premium, pensada para
 * sincronizarse una vez al arrancar la app, no para que la UI la observe
 * de forma continua.
 */
class VerifyPremiumStatusUseCase @Inject constructor(
    private val premiumRepository: PremiumRepository
) {
    suspend operator fun invoke(): Boolean {
        return premiumRepository.readPersistedFlag()
    }
}