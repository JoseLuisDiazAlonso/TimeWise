package com.timewise.app.core.di

import com.timewise.app.data.repository.OnboardingRepositoryImpl
import com.timewise.app.domain.repository.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/*
Esta clase le dice al Hilts que cuando tenga una interface como la debe de implementar*
*
*/

@Module
@InstallIn(SingletonComponent::class)
abstract class OnboardingBindsModule {
    @Binds
    abstract fun bindOnboardingRepository(
        onboardingRepositoryImpl: OnboardingRepositoryImpl
    ): OnboardingRepository
}

