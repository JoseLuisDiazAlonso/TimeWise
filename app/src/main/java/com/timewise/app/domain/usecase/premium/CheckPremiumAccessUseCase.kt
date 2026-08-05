package com.timewise.app.domain.usecase.premium

import com.timewise.app.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Esta clase verifica si el usuario tiene acceso a las funciones premium.
 *
 * 1) Injectamos el constructor(private val PremiumRepository : PremiumRepository)
 * 2) ejecutamos la función fun observe() : Flow<Boolean> que se encuentra en el PremiumRepository
 * que observará si el usuario es o no premium.
 *
 * **/


class CheckPremiumAccessUseCase @Inject constructor(
    private val PremiumRepository : PremiumRepository
) {
    fun observe() : Flow<Boolean> = PremiumRepository.observeisPremium()
}