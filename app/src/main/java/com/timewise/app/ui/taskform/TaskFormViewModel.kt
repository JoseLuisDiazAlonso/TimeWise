package com.timewise.app.ui.taskform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.usecase.CreateTaskUseCase
import com.timewise.app.domain.usecase.DeleteTaskUseCase
import com.timewise.app.domain.usecase.GetTaskByIdUseCase
import com.timewise.app.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
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
                // Antes se calculaba con (date / 86400000) y (date % 86400000) a mano,
                // que ignora la zona horaria del dispositivo. Ahora se convierte
                // correctamente con ZoneId.systemDefault(), igual que al guardar.
                val zonedDateTime = it.dueDate?.let { millis ->
                    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                }

                _uiState.value = _uiState.value.copy(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    dueDate = zonedDateTime?.toLocalDate(),
                    dueTime = zonedDateTime?.toLocalTime(),
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

    fun onDescriptionChanged(newDescription: String) {
        _uiState.value = _uiState.value.copy(description = newDescription)
    }

    fun onSavedClicked() {
        val currentState = _uiState.value
        if (currentState.title.isBlank()) {
            _uiState.value = currentState.copy(titleError = true)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            // ZoneId.systemDefault() en vez de ZoneOffset.UTC: la hora que elige el
            // usuario en el picker es su hora local real, no UTC. Con UTC se guardaba
            // desplazada tantas horas como la diferencia de tu zona horaria con UTC.
            val dueDateMillis = currentState.dueDate
                ?.atTime(currentState.dueTime ?: LocalTime.MIDNIGHT)
                ?.atZone(ZoneId.systemDefault())
                ?.toInstant()
                ?.toEpochMilli()

            if (currentState.id == null) {
                createTaskUseCase(
                    currentState.title,
                    currentState.description,
                    dueDateMillis,
                    currentState.priority,
                    currentState.categoryOption.id.toLong()
                )
            } else {
                updateTaskUseCase(
                    currentState.id,
                    currentState.title,
                    currentState.description,
                    dueDateMillis,
                    currentState.priority,
                    currentState.categoryOption.id.toLong()
                )
            }
            _uiState.update {
                it.copy(isSaving = false, isSaved = true)
            }
        }
    }

    fun onDeleteClicked() {
        val id = _uiState.value.id ?: return
        viewModelScope.launch {
            deleteTaskUseCase(id)
            _uiState.update { it.copy(isDeleted = true) }
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