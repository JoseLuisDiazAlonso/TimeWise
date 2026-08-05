package com.timewise.app.ui.navigation

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.usecase.ads.ShowInterstitialAdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterstitialTriggerViewModel @Inject constructor(
    private val showInterstitialAdUseCase: ShowInterstitialAdUseCase
) : ViewModel() {

    fun maybeShowAd(activity: Activity) {
        viewModelScope.launch {
            showInterstitialAdUseCase.execute(activity)
        }
    }
}