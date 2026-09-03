package com.timewise.app.ui.timeblocking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.R
import com.timewise.app.domain.usecase.premium.CheckPremiumAccessUseCase
import com.timewise.app.domain.usecase.timeblocking.CreateTimeBlockUseCase
import com.timewise.app.domain.usecase.timeblocking.DeleteTimeBlockUseCase
import com.timewise.app.domain.usecase.timeblocking.GetTimeBlocksForDateUseCase
import com.timewise.app.domain.usecase.timeblocking.UpdateTimeBlockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TimeBlockingViewModel @Inject constructor(
    private val getTimeBlocksForDateUseCase: GetTimeBlocksForDateUseCase,
    private val createTimeBlockUseCase: CreateTimeBlockUseCase,
    private val updateTimeBlockUseCase: UpdateTimeBlockUseCase,
    private val deleteTimeBlockUseCase: DeleteTimeBlockUseCase,
    private val checkPremiumAccessUseCase: CheckPremiumAccessUseCase
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _errorMessageRes = MutableStateFlow<Int?>(null)

    val uiState: StateFlow<TimeBlockingUiState> = combine(
        _selectedDate,
        _selectedDate.flatMapLatest { date -> getTimeBlocksForDateUseCase(date) },
        checkPremiumAccessUseCase(),
        _errorMessageRes
    ) { date, blocks, isUnlocked, errorRes ->
        TimeBlockingUiState(
            selectedDate = date,
            timeBlocks = blocks.map { it.toUiModel() }.sortedBy { it.offsetMinutes },
            isPremiumUnlocked = isUnlocked,
            isLoading = false,
            draggingBlockId = null,
            errorMessageRes = errorRes
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = TimeBlockingUiState(isLoading = true)
    )

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun onErrorMessageShown() {
        _errorMessageRes.value = null
    }

    fun onCreateBlock(title: String, start: LocalTime, end: LocalTime, colorHex: String) {
        viewModelScope.launch {
            createTimeBlockUseCase(title, _selectedDate.value, start, end, colorHex)
                .onFailure { _errorMessageRes.value = R.string.time_blocking_overlap_error }
        }
    }

    fun onUpdateBlock(id: Long, title: String, start: LocalTime, end: LocalTime, colorHex: String) {
        viewModelScope.launch {
            updateTimeBlockUseCase(id, title, start, end, colorHex)
                .onFailure { _errorMessageRes.value = R.string.time_blocking_overlap_error }
        }
    }

    fun onDeleteBlock(id: Long) {
        viewModelScope.launch {
            deleteTimeBlockUseCase(id)
        }
    }
}