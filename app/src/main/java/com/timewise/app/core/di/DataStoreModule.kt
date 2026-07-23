package com.timewise.app.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.timewise.app.data.local.datastore.onboardingDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
Esta clase le dice al Hilt como debe de montar el Onboarding.
*
**/

private val Context.userPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.userPreferenceDataStore
    }

    @Provides
    @Singleton
    fun provideOnboardingDataStore (@ApplicationContext context: Context): DataStore<Preferences> =
        context.onboardingDataStore
    }
