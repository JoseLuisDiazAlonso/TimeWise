package com.timewise.app.domain.usecase.premium

import com.timewise.app.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Expone un Flow continuo que indica si el usuario tiene acceso premium
 * en cada momento.
 */
class CheckPremiumAccessUseCase @Inject constructor(
    private val premiumRepository: PremiumRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return premiumRepository.observeIsPremium()
    }
}