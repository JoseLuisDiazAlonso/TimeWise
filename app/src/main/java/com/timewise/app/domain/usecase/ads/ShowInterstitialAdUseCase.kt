package com.timewise.app.domain.usecase.ads

import android.app.Activity
import com.timewise.app.domain.ads.InterstitialAdController
import com.timewise.app.domain.repository.AdFrequencyRepository
import com.timewise.app.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

    class ShowInterstitialAdUseCase @Inject constructor(
        private val adFrequencyRepository: AdFrequencyRepository,
        private val adController: InterstitialAdController,
        private val premiumRepository: PremiumRepository
    ) {
        suspend fun execute(activity: Activity) {
            val isPremiumUser = premiumRepository.observeIsPremium().first()
            if (isPremiumUser) return

            val lastShown = adFrequencyRepository.getLastShownTimestamp()
            val now = System.currentTimeMillis()
            if (now - lastShown < MIN_INTERVAL_ms) return

            adController.showAdIfAvailable(activity)  {

            }
            adFrequencyRepository.updateLastShownTimestamp(now)

        }
        companion object {
            private const val MIN_INTERVAL_ms = 3 * 60 * 1000
        }
    }
