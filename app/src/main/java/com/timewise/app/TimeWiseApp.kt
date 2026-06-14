package com.timewise.app

/**Crearemos una clase que nos permita preparar la infraestructura de la que depende toda la app
 * antes de que se cargue cualquier pantalla.*/

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp // Anotamos la clase con @HiltAndroidApp para que Hilt genere el código necesario para la inyección de dependencias en toda la aplicación.
class TimeWiseApp : Application(), Configuration.Provider {

  @Inject // Inyectamos el HiltWorkerFactory para que WorkManager pueda crear instancias de nuestros workers con las dependencias necesarias.
  lateinit var workerFactory: HiltWorkerFactory // Declaramos una propiedad para el HiltWorkerFactory que se inyectará en tiempo de ejecución.

  override  val workManagerConfiguration: Configuration /**Implementamos la propiedad workManagerConfiguration para proporcionar la configuración de WorkManager,
  utilizando el HiltWorkerFactory para que WorkManager pueda crear instancias de nuestros workers con las dependencias necesarias.*/
      get() = Configuration.Builder()
          .setWorkerFactory(workerFactory)
          .build()
}