package com.timewise.app.domain.ads

import android.app.Activity

/**
 * Esta interface lo que hace es encapsular el ciclo de vida completo del anuncio interstitial
 * de AdMob, cargarlo por delante y mostrarlo cuando se solicite. Separar la interfaz de
 * dominio de la implementación con AdMob es lo que permite que el use case decida si toca
 * mostrar el anuncio.
 *
 * **/

interface InterstitialAdController {
    fun loadAd()
    fun showAdIfAvailable(activity: Activity, onAdDismissed: () -> Unit)
}