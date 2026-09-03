package com.timewise.app.ui.timeblocking

import androidx.annotation.StringRes
import java.time.LocalDate

data class TimeBlockingUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val timeBlocks: List<TimeBlockUiModel> = emptyList(),
    val isPremiumUnlocked: Boolean = false,
    val isLoading: Boolean = true,
    val draggingBlockId: Long? = null,
    @StringRes val errorMessageRes: Int? = null
)