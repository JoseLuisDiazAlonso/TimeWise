package com.timewise.app.ui.taskform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.model.ReminderOption
import com.timewise.app.domain.repository.ReminderScheduler
import com.timewise.app.domain.usecase.CreateTaskUseCase
import com.timewise.app.domain.usecase.DeleteTaskUseCase
import com.timewise.app.domain.usecase.GetTaskByIdUseCase
import com.timewise.app.domain.usecase.UpdateTaskUseCase
import com.timewise.app.domain.usecase.timeblocking.SyncTaskTimeBlockUseCase
import com.timewise.app.ui.timeblocking.toHexString
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

// Antes era LocalTime.MIDNIGHT: al pasar las tareas al calendario, medianoche no es
// un horario útil por defecto. 9:00 encaja mejor con un bloque automático del día.
private val DEFAULT_TASK_TIME: LocalTime = LocalTime.of(9, 0)

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val reminderScheduler: ReminderScheduler,
    private val syncTaskTimeBlockUseCase: SyncTaskTimeBlockUseCase,
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
                val zonedDueDateTime = it.dueDate?.let { millis ->
                    Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault())
                }

                val reminderOption = if (it.reminderAt == null || it.dueDate == null) {
                    ReminderOption.NONE
                } else {
                    val diffMinutes = (it.dueDate - it.reminderAt) / 60_000L
                    ReminderOption.entries.find { option -> option.offsetMinutes == diffMinutes }
                        ?: ReminderOption.AT_TIME
                }

                _uiState.value = _uiState.value.copy(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    dueDate = zonedDueDateTime?.toLocalDate(),
                    dueTime = zonedDueDateTime?.toLocalTime(),
                    reminderOption = reminderOption,
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

    fun onReminderOptionChanged(option: ReminderOption) {
        _uiState.value = _uiState.value.copy(reminderOption = option)
    }

    fun onExactAlarmPermissionDialogDismissed() {
        _uiState.update { it.copy(showExactAlarmPermissionDialog = false) }
    }

    fun onSavedClicked() {
        val currentState = _uiState.value
        if (currentState.title.isBlank()) {
            _uiState.value = currentState.copy(titleError = true)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val dueDateMillis = currentState.dueDate
                ?.atTime(currentState.dueTime ?: DEFAULT_TASK_TIME)
                ?.atZone(ZoneId.systemDefault())
                ?.toInstant()
                ?.toEpochMilli()

            val reminderMillis = if (currentState.reminderOption != ReminderOption.NONE && dueDateMillis != null) {
                dueDateMillis - (currentState.reminderOption.offsetMinutes ?: 0) * 60_000L
            } else {
                null
            }

            val taskId = if (currentState.id == null) {
                createTaskUseCase(
                    currentState.title,
                    currentState.description,
                    dueDateMillis,
                    reminderMillis,
                    currentState.priority,
                    currentState.categoryOption.id.toLong()
                )
            } else {
                updateTaskUseCase(
                    currentState.id,
                    currentState.title,
                    currentState.description,
                    dueDateMillis,
                    reminderMillis,
                    currentState.priority,
                    currentState.categoryOption.id.toLong()
                )
                currentState.id
            }

            if (reminderMillis != null) {
                if (reminderScheduler.canScheduleExactAlarms()) {
                    reminderScheduler.schedule(taskId, currentState.title, reminderMillis)
                } else {
                    _uiState.update { it.copy(showExactAlarmPermissionDialog = true) }
                }
            } else {
                reminderScheduler.cancel(taskId)
            }

            // Toda tarea con fecha y hora aparece siempre como bloque en el calendario.
            syncTaskTimeBlockUseCase.syncForTask(
                taskId = taskId,
                title = currentState.title,
                dueDateMillis = dueDateMillis,
                colorHex = currentState.categoryOption.toHexString()
            )

            _uiState.update {
                it.copy(isSaving = false, isSaved = true)
            }
        }
    }

    fun onDeleteClicked() {
        val id = _uiState.value.id ?: return
        viewModelScope.launch {
            reminderScheduler.cancel(id)
            syncTaskTimeBlockUseCase.removeForTask(id)
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