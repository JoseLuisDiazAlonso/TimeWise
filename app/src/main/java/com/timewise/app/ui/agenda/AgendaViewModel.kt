package com.timewise.app.ui.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.Task
import com.timewise.app.domain.repository.PremiumRepository
import com.timewise.app.domain.usecase.GetTasksUseCase
import com.timewise.app.domain.usecase.ToggleTaskCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

/** Modo de visualización de la agenda: un día, una semana completa, o todas las tareas sin filtrar. */
enum class AgendaViewMode { DAILY, WEEKLY, ALL }

/**
 * Estado único e inmutable que representa todo lo que la pantalla necesita pintar
 * en cada momento. En lugar de varios estados sueltos (Loading, Empty...), usamos
 * un único data class y calculamos isLoading / isEmpty como propiedades derivadas.
 */
data class AgendaUiState(
    val viewMode: AgendaViewMode = AgendaViewMode.DAILY,
    val selectedDate: LocalDate = LocalDate.now(),
    val isLoading: Boolean = true,
    val dailyTasks: List<Task> = emptyList(),
    val weeklyTasks: Map<LocalDate, List<Task>> = emptyMap(),
    val allTasks: List<Task> = emptyList(),
    // Progreso del día: solo se calcula/usa en modo DAILY (ver mockup "3 de 5 pendientes").
    val completedCount: Int = 0,
    val totalCount: Int = 0
) {
    val isEmpty: Boolean
        get() = when (viewMode) {
            AgendaViewMode.DAILY -> dailyTasks.isEmpty()
            AgendaViewMode.WEEKLY -> weeklyTasks.values.all { it.isEmpty() }
            AgendaViewMode.ALL -> allTasks.isEmpty()
        }

    /** Fracción 0f..1f de tareas completadas hoy. 0f si no hay tareas (evita división por 0). */
    val progress: Float
        get() = if (totalCount == 0) 0f else completedCount.toFloat() / totalCount
}

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val premiumRepository: PremiumRepository,
    private val toggleTaskCompletedUseCase: ToggleTaskCompletedUseCase
) : ViewModel() {

    private val _viewMode = MutableStateFlow(AgendaViewMode.DAILY)
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    val uiState: StateFlow<AgendaUiState> = combine(
        _viewMode,
        _selectedDate,
        getTasksUseCase()
    ) { mode, date, tasks ->
        buildUiState(mode, date, tasks)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = AgendaUiState(isLoading = true)
    )

    val isPremium: StateFlow<Boolean> = premiumRepository.observeIsPremium()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = false
        )

    fun setViewMode(mode: AgendaViewMode) {
        _viewMode.value = mode
    }

    fun goToPreviousPeriod() {
        _selectedDate.update { current ->
            when (_viewMode.value) {
                AgendaViewMode.DAILY -> current.minusDays(1)
                AgendaViewMode.WEEKLY -> current.minusWeeks(1)
                AgendaViewMode.ALL -> current
            }
        }
    }

    fun goToNextPeriod() {
        _selectedDate.update { current ->
            when (_viewMode.value) {
                AgendaViewMode.DAILY -> current.plusDays(1)
                AgendaViewMode.WEEKLY -> current.plusWeeks(1)
                AgendaViewMode.ALL -> current
            }
        }
    }

    fun goToToday() {
        _selectedDate.value = LocalDate.now()
    }

    fun onToggleTaskCompleted(task: Task) {
        viewModelScope.launch {
            toggleTaskCompletedUseCase(task)
        }
    }

    private fun buildUiState(
        mode: AgendaViewMode,
        date: LocalDate,
        tasks: List<Task>
    ): AgendaUiState {
        return when (mode) {
            AgendaViewMode.DAILY -> {
                val dayTasks = tasks
                    .filter { it.dueDate != null && it.dueDate.toLocalDate() == date }
                    .sortedBy { it.dueDate }

                AgendaUiState(
                    viewMode = mode,
                    selectedDate = date,
                    isLoading = false,
                    dailyTasks = dayTasks,
                    completedCount = dayTasks.count { it.isCompleted },
                    totalCount = dayTasks.size
                )
            }

            AgendaViewMode.WEEKLY -> {
                val weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val weekDays = (0..6).map { weekStart.plusDays(it.toLong()) }

                val grouped = weekDays.associateWith { day ->
                    tasks
                        .filter { it.dueDate != null && it.dueDate.toLocalDate() == day }
                        .sortedBy { it.dueDate }
                }

                AgendaUiState(
                    viewMode = mode,
                    selectedDate = date,
                    isLoading = false,
                    weeklyTasks = grouped
                )
            }

            AgendaViewMode.ALL -> {
                val sorted = tasks.sortedWith(compareBy(nullsLast()) { it.dueDate })

                AgendaUiState(
                    viewMode = mode,
                    selectedDate = date,
                    isLoading = false,
                    allTasks = sorted
                )
            }
        }
    }

    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}