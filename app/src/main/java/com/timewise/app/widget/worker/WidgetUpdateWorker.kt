package com.timewise.app.widget.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.timewise.app.domain.repository.WidgetUpdater
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Es un Worker programado periódicamente con WorkManager (cada 15-30 minutos) para refrescar
 * el widget aunque no haya cambios activos en los datos, sin que ninguna tarea se haya
 * modificado.
 *
 * Dependencias:
 *  - WidgetUpdater
 * Métodos:
 *  - override suspend fun doWork(): Result
 */
@HiltWorker
class WidgetUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val widgetUpdater: WidgetUpdater,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        widgetUpdater.refreshWidget()
        return Result.success()
    }
}