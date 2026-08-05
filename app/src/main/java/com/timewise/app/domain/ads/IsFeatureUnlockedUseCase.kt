package com.timewise.app.domain.ads

import com.timewise.app.domain.repository.PremiumRepository
import com.timewise.app.domain.repository.TemporaryUnlockRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Esta clase responde a la pregunta si tiene que acceso a las funciones de la app.
 * premium o a desbloqueo temporal activo
 *
 * La función execute() devuelve true si premiumRepository indica premium = true, o si
 * unlockRepository.isUnlockActive() devuelve true.
 * **/

class IsFeatureUnlockedUseCase @Inject constructor(
    private val premiumRepository: PremiumRepository,
    private val temporaryUnlockRepository: TemporaryUnlockRepository
) {
    suspend fun execute() : Boolean {

            val isPremium = premiumRepository.observeisPremium().first()
            val isUnlockActive = temporaryUnlockRepository.isUnlockActive()
            return isPremium || isUnlockActive

    }
}