package com.timewise.app.data.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.timewise.app.domain.ads.RewardedAdController
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Implementación de RewardedAdController usando el SDK de Google Mobile Ads.
 */
class RewardedAdManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : RewardedAdController {

    private var rewardedAd: RewardedAd? = null

    override fun loadAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            ADUNIT_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    override fun showAd(activity: Activity, onRewardEarned: () -> Unit, onAdClosed: () -> Unit) {
        val ad = rewardedAd
        if (ad == null) {
            onAdClosed()
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                loadAd()
                onAdClosed()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                rewardedAd = null
                loadAd()
                onAdClosed()
            }
        }

        ad.show(activity) {
            onRewardEarned()
        }
    }

    private companion object {
        const val ADUNIT_ID = "ca-app-pub-3940256099942544/5224354917" // ID de test; cambiar antes de publicar
    }
}