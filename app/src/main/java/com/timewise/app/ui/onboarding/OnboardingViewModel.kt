package com.timewise.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.usecase.onboarding.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Esta clase lo que hace es configurar la vista del onboarding. Es decir, crea el modelo
para la vista del Onboarding*
*
*/
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) :ViewModel() {
    fun onboardingFinished(onFinished: () -> Unit) {
        viewModelScope.launch {
            completeOnboardingUseCase()
            onFinished()
        }
    }
}