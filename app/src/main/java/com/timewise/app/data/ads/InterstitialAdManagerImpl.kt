package com.timewise.app.data.ads

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.timewise.app.domain.ads.InterstitialAdController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InterstitialAdManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : InterstitialAdController {
    private var interstitialAd: InterstitialAd? = null

    override fun loadAd() {
        InterstitialAd.load(context, "YOUR_AD_UNIT_ID", AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                // El anuncio se ha cargado correctamente
                interstitialAd = ad
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                // El anuncio no se pudo cargar
                interstitialAd = null
            }
        })
    }

    override fun showAdIfAvailable(activity: android.app.Activity, onAdDismissed: () -> Unit) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // El anuncio se ha cerrado
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    // El anuncio no se pudo mostrar
                    onAdDismissed()
                }
            }
            ad.show(activity)
        } else {
            // No hay anuncio disponible, simplemente llamamos al callback
            onAdDismissed()
        }
    }
}