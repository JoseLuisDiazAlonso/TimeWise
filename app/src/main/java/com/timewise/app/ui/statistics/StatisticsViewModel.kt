package com.timewise.app.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.ads.IsFeatureUnlockedUseCase
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.usecase.GetTimeStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getTimeStatsUseCase: GetTimeStatsUseCase,
    private val isFeatureUnlockedUseCase: IsFeatureUnlockedUseCase
) : ViewModel() {

    private val _period = MutableStateFlow(TimeStatsPeriod.SEMANAL)
    private val _isUnlocked = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            _isUnlocked.value = isFeatureUnlockedUseCase.execute()
        }
    }

    val uiState: StateFlow<StatisticsUiState> = combine(
        _period.flatMapLatest { period ->
            getTimeStatsUseCase(period, LocalDate.now())
        },
        _isUnlocked
    ) { summary, isUnlocked ->
        StatisticsUiState(
            period = summary.period,
            periodStart = summary.periodStart,
            periodEnd = summary.periodEnd,
            categoryStats = summary.categoryStats,
            totalTrackedMinutes = summary.totalTrackedMinutes,
            isLoading = false,
            isUnlocked = isUnlocked
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = StatisticsUiState(isLoading = true)
    )

    fun onPeriodSelected(period: TimeStatsPeriod) {
        _period.value = period
    }
}