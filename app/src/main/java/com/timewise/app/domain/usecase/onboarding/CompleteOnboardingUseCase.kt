package com.timewise.app.domain.usecase.onboarding

import com.timewise.app.domain.repository.OnboardingRepository
import javax.inject.Inject

/*
Esta clase lo que hace es marcar el onboarding como completado.*
**/

/*
private val onboardingRepository.
fun invoke()***/

class CompleteOnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke() = onboardingRepository.setOnboardingCompleted()
}