package com.timewise.app

/**Crearemos una clase que nos permita preparar la infraestructura de la que depende toda la app
 * antes de que se cargue cualquier pantalla.*/

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.timewise.app.data.local.locale.AppLocaleManager
import com.timewise.app.domain.usecase.settings.GetUserPreferencesUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp // Anotamos la clase con @HiltAndroidApp para que Hilt genere el código necesario para la inyección de dependencias en toda la aplicación.
class TimeWiseApp : Application(), Configuration.Provider {

  @Inject // Inyectamos el HiltWorkerFactory para que WorkManager pueda crear instancias de nuestros workers con las dependencias necesarias.
  lateinit var workerFactory: HiltWorkerFactory // Declaramos una propiedad para el HiltWorkerFactory que se inyectará en tiempo de ejecución.

  @Inject
  lateinit var getuserPreferencesUseCase: GetUserPreferencesUseCase //Nos permite leer el idioma guardado

  @Inject
  lateinit var appLocaleManager: AppLocaleManager //Nos permite cambiar el idioma

    // Scope propio de la Application: no existe viewModelScope aquí porque esto no es un ViewModel.
    // SupervisorJob evita que un fallo cancele el resto de tareas; Dispatchers.Default porque no toca UI.
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            val prefs = getuserPreferencesUseCase().first()
            appLocaleManager.applylanguage(prefs.language)
        }
    }

  override  val workManagerConfiguration: Configuration /**Implementamos la propiedad workManagerConfiguration para proporcionar la configuración de WorkManager,
  utilizando el HiltWorkerFactory para que WorkManager pueda crear instancias de nuestros workers con las dependencias necesarias.*/
      get() = Configuration.Builder()
          .setWorkerFactory(workerFactory)
          .build()
}