package com.timewise.app.data.notifications

/**
 * Esta clase lo que hace es recibir y mostrar la notificación
 *
 * **/

import android.Manifest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.timewise.app.core.di.NotificationsModule
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {
    @Inject lateinit var notificationManagerCompat: NotificationManagerCompat
    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra( EXTRA_REMINDER_ID, -1)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Recordatorio"

        if (reminderId == -1L)  return
        val notification = NotificationCompat.Builder(context, NotificationsModule.CHANNEL_TASKS)
            .setContentTitle(title)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(reminderId.toInt(), notification)
    }

    companion object {
        const val EXTRA_REMINDER_ID = "EXTRA_REMINDER_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }

}