package com.timewise.app.data.notifications

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Esta clase comprueba el estado del permiso del sistema. Lo que hace la clase es primero
 * compruebar si el sistema de notificaciones está habilitado.
 * Después ejecuta el Intent de la aplicación de Notificaciones.
 *
 * **/

class NotificationPermissionChecker @Inject constructor(@ApplicationContext private val context: Context) {
    fun areSystemNotificationsEnabled() : Boolean =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    fun openSystemNotificationsSettings() : Intent =
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
}

