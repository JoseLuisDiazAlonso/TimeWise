package com.timewise.app.ui.taskform

/**Esta es la clase que recibe los eventos del usuario y los pasa al caso de uso correspondiente*/

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.Priority

import com.timewise.app.domain.usecase.CreateTaskUseCase
import com.timewise.app.domain.usecase.GetTaskByIdUseCase
import com.timewise.app.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
   private val _uiState = MutableStateFlow(TaskFormUiState())
    val uiState: StateFlow<TaskFormUiState> = _uiState.asStateFlow()

    init {
        val id: Long? = savedStateHandle["taskId"]
        if (id != null) loadTask(id)

    }

    private fun loadTask(id: Long) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase(id).first()
            task?.let {
                _uiState.value = _uiState.value.copy(
                    id = it.id,
                    title = it.title,
                    dueDate = it.dueDate?.let { date -> LocalDate.ofEpochDay(date / 86400000) },
                    dueTime = it.dueDate?.let { date -> LocalTime.ofSecondOfDay((date % 86400000) / 1000) },
                    priority = it.priority,
                    categoryOption = availableCategories.find { category -> category.id.toLong() == it.categoryId }
                        ?: availableCategories.first()
                )

            }

        }

    }

    fun onTitleChanged(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle, titleError = false)
    }

    fun onSavedClicked() {
        val currentState = _uiState.value
        if (currentState.title.isBlank()) {
            _uiState.value = currentState.copy(titleError = true)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            if (currentState.id == null) {
                createTaskUseCase(
                    currentState.title,
                    currentState.dueDate?.atTime(currentState.dueTime ?: LocalTime.MIDNIGHT)
                        ?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
                    currentState.priority,
                    currentState.categoryOption.id.toLong()
                )

            } else {
                updateTaskUseCase(
                    currentState.id,
                    currentState.title,
                    currentState.dueDate?.atTime(currentState.dueTime ?: LocalTime.MIDNIGHT)
                        ?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
                    currentState.priority,
                    currentState.categoryOption.id.toLong()
                )
            }
            _uiState.update {
                it.copy(isSaving = false, isSaved = true)

            }
        }
    }
    fun onDueDateChanged(newDate: LocalDate?) {
        _uiState.value = _uiState.value.copy(dueDate = newDate)
    }
    fun onDueTimeChanged(newTime: LocalTime?) {
        _uiState.value = _uiState.value.copy(dueTime = newTime)
    }
    fun onPriorityChanged(newPriority: Priority) {
        _uiState.value = _uiState.value.copy(priority = newPriority)
    }
    fun onCategoryChanged(newCategory: CategoryOption) {
        _uiState.value = _uiState.value.copy(categoryOption = newCategory)
    }

}