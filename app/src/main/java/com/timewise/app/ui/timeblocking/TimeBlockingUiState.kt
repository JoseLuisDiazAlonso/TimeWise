package com.timewise.app.ui.timeblocking

import java.time.LocalDate

/**
 * Esta clase es un estado inmutable expuesto por el ViewModel a traves de StateFlow
 *
 * Propiedades:
 *  - selectedDate de tipo LocalDate
 *  - timeBlocks de tipo List<TimeBlockUiModel>
 *  - isPremiumUnlocked de tipo Boolean
 *  - isLoading de tipo Boolean
 *  - draggingBlockId de tipo Long?
 *  - errorMessage de tipo String?**/

class TimeBlockingUiState (
    val selectedDate: LocalDate,
    val timeBlocks: List<TimeBlockUiModel>,
    val isPremiumUnlocked: Boolean,
    val isLoading: Boolean,
    val draggingBlockId: Long?,
    val errorMessage: String?
) {
    fun copy(selectedDate: LocalDate, isLoading: Boolean, timeBlocks: Any): TimeBlockingUiState {

        return TimeBlockingUiState(
            selectedDate = selectedDate,
            timeBlocks = this.timeBlocks,
            isPremiumUnlocked = this.isPremiumUnlocked,
            isLoading = isLoading,
            draggingBlockId = this.draggingBlockId,
            errorMessage = this.errorMessage
        )
    }

    fun onEach(any: Any): TimeBlockingUiState {
        return TimeBlockingUiState(
            selectedDate = this.selectedDate,
            timeBlocks = this.timeBlocks,
            isPremiumUnlocked = this.isPremiumUnlocked,
            isLoading = this.isLoading,
            draggingBlockId = this.draggingBlockId,
            errorMessage = this.errorMessage
        )

    }

    fun copy(timeBlocks: Any, isLoading: Boolean): TimeBlockingUiState {
        return TimeBlockingUiState(
            selectedDate = this.selectedDate,
            timeBlocks = this.timeBlocks,
            isPremiumUnlocked = this.isPremiumUnlocked,
            isLoading = isLoading,
            draggingBlockId = this.draggingBlockId,
            errorMessage = this.errorMessage
        )

    }
}