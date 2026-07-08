package com.timewise.app.ui.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timewise.app.domain.model.Task
import com.timewise.app.domain.usecase.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

/** Modo de visualización de la agenda: un solo día o una semana completa. */
enum class AgendaViewMode { DAILY, WEEKLY }

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
    val weeklyTasks: Map<LocalDate, List<Task>> = emptyMap()
) {
    val isEmpty: Boolean
        get() = when (viewMode) {
            AgendaViewMode.DAILY -> dailyTasks.isEmpty()
            AgendaViewMode.WEEKLY -> weeklyTasks.values.all { it.isEmpty() }
        }
}

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    // Fuente del modo de vista seleccionado por el usuario (Diario/Semanal)
    private val _viewMode = MutableStateFlow(AgendaViewMode.DAILY)

    // Fuente de la fecha de referencia (el día concreto, o cualquier día de la semana vista)
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    /**
     * combine() se suscribe a las 3 fuentes (modo, fecha, tareas) y se vuelve a
     * ejecutar automáticamente cada vez que cualquiera de ellas emite un nuevo valor.
     */
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

    fun setViewMode(mode: AgendaViewMode) {
        _viewMode.value = mode
    }

    fun goToPreviousPeriod() {
        _selectedDate.update { current ->
            when (_viewMode.value) {
                AgendaViewMode.DAILY -> current.minusDays(1)
                AgendaViewMode.WEEKLY -> current.minusWeeks(1)
            }
        }
    }

    fun goToNextPeriod() {
        _selectedDate.update { current ->
            when (_viewMode.value) {
                AgendaViewMode.DAILY -> current.plusDays(1)
                AgendaViewMode.WEEKLY -> current.plusWeeks(1)
            }
        }
    }

    fun goToToday() {
        _selectedDate.value = LocalDate.now()
    }

    // Construye el AgendaUiState a partir de los 3 valores actuales
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
                    dailyTasks = dayTasks
                )
            }

            AgendaViewMode.WEEKLY -> {
                // Ancla la semana al lunes anterior (o el mismo día si ya es lunes)
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
        }
    }

    /** Convierte el timestamp (epoch millis) guardado en Room a un LocalDate del huso horario del dispositivo. */
    private fun Long.toLocalDate(): LocalDate =
        Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}