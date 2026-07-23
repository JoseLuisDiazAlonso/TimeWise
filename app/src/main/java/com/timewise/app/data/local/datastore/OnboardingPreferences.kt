package com.timewise.app.data.local.datastore

/*
Este archivo lo que hace es garantizar una única instancia de datos en DataStore por proceso*
*
*/
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.onboardingDataStore by preferencesDataStore(name = "onboarding_preferences")
