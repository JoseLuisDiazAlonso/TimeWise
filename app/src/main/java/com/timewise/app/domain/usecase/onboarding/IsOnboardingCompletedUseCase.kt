package com.timewise.app.domain.usecase.onboarding

import com.timewise.app.domain.repository.OnboardingRepository
import javax.inject.Inject

/**
 * Esta clase lo que hara es determinar que ocurrirá cuando el onboarding ya este completado.
 * **/

class IsOnboardingCompletedUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke() = onboardingRepository.isOnboardingCompleted()
}