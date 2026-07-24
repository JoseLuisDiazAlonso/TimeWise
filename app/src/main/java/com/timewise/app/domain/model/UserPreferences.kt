package com.timewise.app.domain.model

/**
 * Este archivo es un modelo inmutable de datos que muestra los ajustes que se necesita para
 * observar a la vez. Esto evita que el ViewModel tenga que combinar tres Flows distintos
 * (val language, val notificationsEnabled, val isPremium)
 * manualmente en la mayoría de los casos
 * **/

data class UserPreferences(
    val language: AppLanguage,
    val notificationsEnabled: Boolean,
    val isPremium: Boolean
)
