package com.timewise.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.usecase.onboarding.IsOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * El destino inicial depende un valor asíncrono. Mientras esto se resuelve para evitar que
 * se muestre una pantalla en blanco o un parpadeo entre agenda y onboarding mantenemos visible
 * el Splash Screen
 * **/

/**
 * Etiqueta. @HiltViewModel
 * 1) Se injecta el constructor (isOnboardingCompletedUseCase) en el ViewModel()
 * 2) El bloque es ViewModel() con la variable startDestination**/

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    isOnboardingCompletedUseCase: IsOnboardingCompletedUseCase
) : ViewModel() {
    val startDestination = isOnboardingCompletedUseCase()
        .map { completed -> if (completed) "agenda" else "onboarding"}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue =  null

        )
}