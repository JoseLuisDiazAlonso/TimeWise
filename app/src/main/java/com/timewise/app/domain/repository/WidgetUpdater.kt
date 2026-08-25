package com.timewise.app.domain.repository

/**
 * Lo que hace este interface es determinar el refresco del widget de tal manera que el
 * domain no conozca la existencia del Glance.
 */

interface WidgetUpdater {
    suspend fun refreshWidget()
}