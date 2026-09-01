package com.timewise.app

/**Crearemos una clase que nos permita preparar la infraestructura de la que depende toda la app
 * antes de que se cargue cualquier pantalla.*/

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.timewise.app.data.local.locale.AppLocaleManager
import com.timewise.app.data.local.locale.LocaleHelper
import com.timewise.app.domain.usecase.settings.GetUserPreferencesUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class TimeWiseApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var getuserPreferencesUseCase: GetUserPreferencesUseCase

    @Inject
    lateinit var appLocaleManager: AppLocaleManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Mismo wrap que en MainActivity, para que componentes que lean recursos desde
    // el contexto de la Application (no solo la Activity) respeten el idioma.
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.wrap(base))
    }

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            val prefs = getuserPreferencesUseCase().first()
            appLocaleManager.applylanguage(prefs.language)
        }
    }

    override  val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}