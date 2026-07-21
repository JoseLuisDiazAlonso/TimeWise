package com.timewise.app.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

import com.timewise.app.domain.repository.ReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AlarmManagerReminderScheduler @Inject constructor(
    private val alarmManager: AlarmManager,
    @ApplicationContext private val context: Context,
) : ReminderScheduler {

    override fun schedule(reminderId: Long, title: String, triggerAtMillis: Long) {
        val pendingIntent = buildPendingIntent(reminderId, title)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent,
        )
    }

    override fun schedule(reminderId: Long, title: String) {
        TODO("Not yet implemented")
    }

    override fun cancel(reminderId: Long) {
        val pendingIntent = buildPendingIntent(reminderId, null)
        alarmManager.cancel(pendingIntent)
    }

    private fun buildPendingIntent(reminderId: Long, title: String?, forCancel: Boolean = false,): PendingIntent {
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra(ReminderBroadcastReceiver.EXTRA_REMINDER_ID, reminderId)
            title?.let { putExtra(ReminderBroadcastReceiver.EXTRA_TITLE, it) }
        }
        return PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            if (forCancel) PendingIntent.FLAG_NO_CREATE else PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    }
}