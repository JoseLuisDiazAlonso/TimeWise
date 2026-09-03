package com.timewise.app.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.timewise.app.domain.repository.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmManagerReminderScheduler @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context,
) : ReminderScheduler {

    override fun schedule(reminderId: Long, title: String, triggerAtMillis: Long) {
        if (!canScheduleExactAlarms()) return

        val pendingIntent = buildSchedulePendingIntent(reminderId, title)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent,
        )
    }

    override fun cancel(reminderId: Long) {
        val pendingIntent = buildCancelPendingIntent(reminderId)
        pendingIntent?.let { alarmManager.cancel(it) }
    }

    override fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    // Nunca devuelve null: FLAG_UPDATE_CURRENT siempre crea uno si no existe.
    private fun buildSchedulePendingIntent(reminderId: Long, title: String): PendingIntent {
        val intent = buildIntent(reminderId, title)
        return PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // Puede devolver null: FLAG_NO_CREATE no crea uno nuevo si no existe ya.
    private fun buildCancelPendingIntent(reminderId: Long): PendingIntent? {
        val intent = buildIntent(reminderId, title = null)
        return PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildIntent(reminderId: Long, title: String?): Intent {
        return Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderBroadcastReceiver.EXTRA_REMINDER_ID, reminderId)
            title?.let { putExtra(ReminderBroadcastReceiver.EXTRA_TITLE, it) }
        }
    }
}