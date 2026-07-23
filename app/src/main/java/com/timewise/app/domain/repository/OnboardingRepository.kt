package com.timewise.app.domain.repository

import kotlinx.coroutines.flow.Flow

/*
Esta interface lo que hace es reconocer si el usuario ya ha visto el onboarding.*
**/

interface OnboardingRepository {
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted()
}
