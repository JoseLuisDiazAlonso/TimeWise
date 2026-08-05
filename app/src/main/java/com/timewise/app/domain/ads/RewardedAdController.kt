package com.timewise.app.domain.ads

import android.app.Activity

/**
 * Esta interface define el contrato para mostrar un anunció recompensando al usuario sin que el
 * resto de la app conozca AdMob.
 *
 * **/

interface RewardedAdController {
    fun loadAd() //Precarga un RewardedAd para tenerlo listo antes que el usuario lo necesite
    fun showAd (activity: Activity, onRewardEarned: () -> Unit, onAdClosed: () -> Unit) /*
    muestra el anuncio onRewardedEarned solo se invoca si el usuario ve el vídeo completo.
    onAdClosed se invoca cuando el anuncio se ha cerrado.*/
}