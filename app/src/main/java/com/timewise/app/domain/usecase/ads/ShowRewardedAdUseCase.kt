package com.timewise.app.domain.usecase.ads

import android.app.Activity
import com.timewise.app.domain.ads.RewardedAdController
import com.timewise.app.domain.repository.TemporaryUnlockRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject


class ShowRewardedAdUseCase @Inject constructor(
    private val rewardedAdController: RewardedAdController,
    private val temporaryUnlockRepository: TemporaryUnlockRepository
)
 {
     suspend fun execute(activity: Activity, durationMs: Long = 24 * 60 * 60 * 1000L) {
         var rewaredEarned = false

         suspendCancellableCoroutine <Unit> { continuation ->
             rewardedAdController.showAd(
                 activity,
                 onRewardEarned = {
                     rewaredEarned = true
                 },
                 onAdClosed = {
                     if (continuation.isActive) {
                         continuation.resumeWith(Result.success(Unit))
                     }
                 }
             )
         }
         if (rewaredEarned) {
             temporaryUnlockRepository.grantTemporaryUnlock(durationMs)
         }
     }
}