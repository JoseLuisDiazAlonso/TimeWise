package com.timewise.app.ui.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.timewise.app.R
import com.timewise.app.domain.model.Task
import com.timewise.app.ui.agenda.components.AgendaDateHeader
import com.timewise.app.ui.agenda.components.AgendaEmptyState
import com.timewise.app.ui.agenda.components.AgendaLoadingIndicator
import com.timewise.app.ui.agenda.components.AgendaModeSelector
import com.timewise.app.ui.agenda.components.DailyAgendaList
import com.timewise.app.ui.agenda.components.WeeklyAgendaList

@Composable
fun AgendaScreen(
    onTaskClick: (Task) -> Unit = {},
    onAddTaskClick: () -> Unit = {},
    viewModel: AgendaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    AgendaContent(
        uiState = uiState,
        onModeSelected = viewModel::setViewMode,
        onPreviousClick = viewModel::goToPreviousPeriod,
        onNextClick = viewModel::goToNextPeriod,
        onTodayClick = viewModel::goToToday,
        onTaskClick = onTaskClick,
        onAddTaskClick = onAddTaskClick
    )
}

@Composable
private fun AgendaContent(
    uiState: AgendaUiState,
    onModeSelected: (AgendaViewMode) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTodayClick: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onAddTaskClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        AgendaModeSelector(
            selectedMode = uiState.viewMode,
            onModeSelected = onModeSelected
        )

        val weekRange = if (uiState.viewMode == AgendaViewMode.WEEKLY && uiState.weeklyTasks.isNotEmpty()) {
            uiState.weeklyTasks.keys.first() to uiState.weeklyTasks.keys.last()
        } else {
            null
        }

        AgendaDateHeader(
            viewMode = uiState.viewMode,
            selectedDate = uiState.selectedDate,
            weekRange = weekRange,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            onTodayClick = onTodayClick
        )

        when {
            uiState.isLoading -> AgendaLoadingIndicator(modifier = Modifier.fillMaxSize())

            uiState.isEmpty -> AgendaEmptyState(
                message = if (uiState.viewMode == AgendaViewMode.DAILY) {
                    stringResource(R.string.agenda_empty_daily_state)
                } else {
                    stringResource(R.string.agenda_empty_weekly_state)
                },
                ctaText = stringResource(R.string.task_add),
                onAddTaskClick = onAddTaskClick,
                modifier = Modifier.fillMaxSize()
            )

            uiState.viewMode == AgendaViewMode.DAILY -> DailyAgendaList(
                tasks = uiState.dailyTasks,
                onTaskClick = onTaskClick,
                modifier = Modifier.fillMaxSize()
            )

            else -> WeeklyAgendaList(
                tasksByDay = uiState.weeklyTasks,
                onTaskClick = onTaskClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}