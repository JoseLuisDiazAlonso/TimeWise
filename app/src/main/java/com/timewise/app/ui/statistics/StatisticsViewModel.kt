package com.timewise.app.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.domain.model.TimeStatsSummary
import com.timewise.app.domain.usecase.GetTimeStatsUseCase
import com.timewise.app.domain.usecase.premium.CheckPremiumAccessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

/**
 * Esta clase combina tres fuentes reactivas: el periodo seleccionado, la fecha de referencia
 * y el estado premium. Cuando cambia el periodo o la fecha se relanza automáticamente la
 * consulta de estadísticas gracias a flatMapLatest.
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getTimeStatsUseCase: GetTimeStatsUseCase,
    private val checkPremiumAccessUseCase: CheckPremiumAccessUseCase
) : ViewModel() {

    private val _period = MutableStateFlow(TimeStatsPeriod.SEMANAL)
    private val _referenceDate = MutableStateFlow(LocalDate.now())

    private val statsFlow: Flow<TimeStatsSummary> = combine(_period, _referenceDate) { period, date ->
        period to date
    }.flatMapLatest { (period, date) ->
        getTimeStatsUseCase(period, date)
    }

    val uiState: StateFlow<StatisticsUiState> = combine(
        statsFlow,
        checkPremiumAccessUseCase()
    ) { summary, isPremium ->
        StatisticsUiState(
            period = summary.period,
            referenceDate = _referenceDate.value,
            categoryStats = summary.categoryStats,
            totalMinutes = summary.totalTrackedMinutes,
            isPremiumUnlocked = isPremium,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StatisticsUiState(
            period = _period.value,
            referenceDate = _referenceDate.value,
            categoryStats = emptyList(),
            totalMinutes = 0L,
            isPremiumUnlocked = false,
            isLoading = true
        )
    )

    fun onPeriodSelected(period: TimeStatsPeriod) {
        _period.value = period
    }

    fun onPreviousPeriod() {
        _referenceDate.value = when (_period.value) {
            TimeStatsPeriod.SEMANAL -> _referenceDate.value.minusWeeks(1)
            TimeStatsPeriod.MENSUAL -> _referenceDate.value.minusMonths(1)
        }
    }

    fun onNextPeriod() {
        _referenceDate.value = when (_period.value) {
            TimeStatsPeriod.SEMANAL -> _referenceDate.value.plusWeeks(1)
            TimeStatsPeriod.MENSUAL -> _referenceDate.value.plusMonths(1)
        }
    }
}