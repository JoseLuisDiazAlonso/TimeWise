package com.timewise.app.ui.agenda.components.ads

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.ads.IsFeatureUnlockedUseCase
import com.timewise.app.domain.usecase.ads.ShowRewardedAdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Puente entre la pantalla que ofrece el desbloqueo y los casos de uso.
 * Solo expone estado observable y delega en ShowRewardedAdUseCase / IsFeatureUnlockedUseCase.
 */
@HiltViewModel
class RewardedAdViewModel @Inject constructor(
    private val showRewardedAdUseCase: ShowRewardedAdUseCase,
    private val isFeatureUnlockedUseCase: IsFeatureUnlockedUseCase
) : ViewModel() {

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    init {
        viewModelScope.launch {
            _isUnlocked.value = isFeatureUnlockedUseCase.execute()
        }
    }

    fun onWatchAdClicked(activity: Activity) {
        viewModelScope.launch {
            showRewardedAdUseCase.execute(activity)
            _isUnlocked.value = isFeatureUnlockedUseCase.execute()
        }
    }
}