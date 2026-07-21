package com.timewise.app.core.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.timewise.app.data.notifications.AlarmManagerReminderScheduler
import com.timewise.app.domain.repository.ReminderScheduler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    const val CHANNEL_TASKS   = "channel_tasks"
    const val CHANNEL_EVENTS  = "channel_events"
    const val CHANNEL_GENERAL = "channel_general"

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val manager = NotificationManagerCompat.from(context)
        val channels = listOf(
            NotificationChannel(CHANNEL_TASKS,   "Recordatorios de tareas",   NotificationManager.IMPORTANCE_HIGH),
            NotificationChannel(CHANNEL_EVENTS,  "Recordatorios de eventos",  NotificationManager.IMPORTANCE_HIGH),
            NotificationChannel(CHANNEL_GENERAL, "General", NotificationManager.IMPORTANCE_DEFAULT),
        )
        manager.createNotificationChannels(channels)
        return manager
    }
    @Provides
    @Singleton
    fun provideAlarmManager (
        @ApplicationContext context: Context
    ) = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ReminderBindingsModule {
    @Binds
    abstract fun bindReminderScheduler (
        impl : AlarmManagerReminderScheduler,
    ) : ReminderScheduler
}