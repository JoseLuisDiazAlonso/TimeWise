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
import javax.inject.Qualifier
import javax.inject.Singleton

/*
Esta clase le dice a Hilt cómo debe montar los distintos DataStore de la app.
*/

private val Context.userPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
private const val SETTINGS_PREFERENCES = "timewise_settings"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = SETTINGS_PREFERENCES)

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserPreferenceDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OnboardingDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsDataStore

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @UserPreferenceDataStore
    fun provideUserPreferenceDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.userPreferenceDataStore
    }

    @Provides
    @Singleton
    @OnboardingDataStore
    fun provideOnboardingDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.onboardingDataStore

    @Provides
    @Singleton
    @SettingsDataStore
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore
}