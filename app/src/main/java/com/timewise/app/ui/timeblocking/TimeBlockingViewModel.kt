package com.timewise.app.ui.timeblocking

import androidx.lifecycle.ViewModel
import com.timewise.app.domain.usecase.premium.CheckPremiumAccessUseCase
import com.timewise.app.domain.usecase.timeblocking.CreateTimeBlockUseCase
import com.timewise.app.domain.usecase.timeblocking.DeleteTimeBlockUseCase
import com.timewise.app.domain.usecase.timeblocking.GetTimeBlocksForDateUseCase
import com.timewise.app.domain.usecase.timeblocking.MoveTimeBlockUseCase
import com.timewise.app.domain.usecase.timeblocking.UpdateTimeBlockUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

/**
 * Esta clase oquesta la carga reactiva de bloques del día seleccionado y expone los eventos de
 * creación, edición, borrado y arrastre. Observa un único Flow a la vez: al cambiar de fecha,
 * cancela la suscripción anterior y arranca una nueva.
 *
 * Propiedades:
 *  - getTimeBlocksForDateUse case de tipo GetTimeBlocksForDateUseCase que es inyectado por el
 *  constructor
 *  - createTimeBlockUseCase de tipo CreateTimeBlockUseCase que es inyectado por el constructor
 *  - updateTimeBlockUseCase de tipo UpdateTimeBlockUseCase que es inyectado por el constructor
 *  - moveTimeBlockUseCase de tipo MoveTimeBlockUseCase que es inyectado por el constructor
 *  - deleteTimeBlockUseCase de tipo DeleteTimeBlockUseCase que es inyectado por el constructor
 *  - checkPremiumAccessUseCase de tipo CheckPremiumAccessUseCase que es inyectado por el constructor
 *  - _uiState de tipo MutableStateFlow<TimeBlockingUiState>
 *  - uiState de tipo StateFlow<TimeBlockingUiState>
 *
 * Funciones:
 *  - fun onDateSelected (date: LocalDate): Unit. Esta función se llama cuando se selecciona una nueva
 *  - fun onBlockDragEnd (blockId: Long, newStartMinutes: Int, newEndMinutes: Int) : Unit. Esta función se llama cuando se termina de arrastrar un bloque.
 *  - fun OnCreateBlockClick (title: String, startTime: LocalTime, endTime: LocalTime,
 *  categoryColor: String): Unit. Esta función se llama cuando se crea un nuevo bloque.
 *  - fun onDeleteBlock (id: Long): Unit. Esta función se llama cuando se elimina un bloque.
 *  - fun onErrorMessageShown(): Unit. Esta función se llama cuando se muestra un mensaje de error.**/

class TimeBlockingViewModel @Inject constructor(
    private val getTimeBlocksForDateUseCase: GetTimeBlocksForDateUseCase,
    private val createTimeBlockUseCase: CreateTimeBlockUseCase,
    private val updateTimeBlockUseCase: UpdateTimeBlockUseCase,
    private val moveTimeBlockUseCase: MoveTimeBlockUseCase,
    private val deleteTimeBlockUseCase: DeleteTimeBlockUseCase,
    private val checkPremiumAccessUseCase: CheckPremiumAccessUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        TimeBlockingUiState(
            selectedDate = LocalDate.now(),
            timeBlocks = emptyList(),
            isPremiumUnlocked = false,
            isLoading = true,
            draggingBlockId = null,
            errorMessage = null
        )
    )
    val uiState: StateFlow<TimeBlockingUiState> = _uiState

    fun onDateSelected(date: LocalDate) {
        val timeBlocks: List<Any> = emptyList()
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            isLoading = true,
            timeBlocks = timeBlocks
        )
            .onEach { timeBlocks: List<Any> ->
                _uiState.value = _uiState.value.copy(
                    timeBlocks = timeBlocks,
                    isLoading = false
                )
            }
    }

    fun onErrorMessageShown() {
        TODO("Not yet implemented")
    }

    fun onBlockDragEnd(id: Long, newOffsetMinutes: Int, newDurationEndMinutes: Int) {

    }

    fun onCreateBlock(title: Any, start: Any, end: Any, color: Any) {
        TODO("Not yet implemented")
    }

}