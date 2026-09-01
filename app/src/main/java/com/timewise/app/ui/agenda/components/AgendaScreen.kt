package com.timewise.app.ui.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timewise.app.R
import com.timewise.app.domain.model.Task
import com.timewise.app.ui.agenda.components.AgendaDateHeader
import com.timewise.app.ui.agenda.components.AgendaEmptyState
import com.timewise.app.ui.agenda.components.AgendaLoadingIndicator
import com.timewise.app.ui.agenda.components.AgendaModeSelector
import com.timewise.app.ui.agenda.components.AgendaProgressBar
import com.timewise.app.ui.agenda.components.DailyAgendaList
import com.timewise.app.ui.agenda.components.WeeklyAgendaList
import com.timewise.app.ui.agenda.components.ads.BannerAdView

private val MAX_CONTENT_WIDTH = 600.dp

@Composable
fun AgendaScreen(
    onTaskClick: (Task) -> Unit = {},
    onAddTaskClick: () -> Unit = {},
    viewModel: AgendaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isPremium by viewModel.isPremium.collectAsStateWithLifecycle()

    AgendaContent(
        uiState = uiState,
        isPremium = isPremium,
        onModeSelected = viewModel::setViewMode,
        onPreviousClick = viewModel::goToPreviousPeriod,
        onNextClick = viewModel::goToNextPeriod,
        onTodayClick = viewModel::goToToday,
        onTaskClick = onTaskClick,
        onAddTaskClick = onAddTaskClick,
        onToggleComplete = viewModel::onToggleTaskCompleted
    )
}

@Composable
private fun AgendaContent(
    uiState: AgendaUiState,
    isPremium: Boolean,
    onModeSelected: (AgendaViewMode) -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTodayClick: () -> Unit,
    onTaskClick: (Task) -> Unit,
    onAddTaskClick: () -> Unit,
    onToggleComplete: (Task) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.task_add))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AgendaModeSelector(
                selectedMode = uiState.viewMode,
                onModeSelected = onModeSelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = MAX_CONTENT_WIDTH)
                    .align(Alignment.CenterHorizontally)
            )

            if (uiState.viewMode != AgendaViewMode.ALL) {
                val weekRange = if (uiState.viewMode == AgendaViewMode.WEEKLY && uiState.weeklyTasks.isNotEmpty()) {
                    uiState.weeklyTasks.keys.first() to uiState.weeklyTasks.keys.last()
                } else null

                AgendaDateHeader(
                    viewMode = uiState.viewMode,
                    selectedDate = uiState.selectedDate,
                    weekRange = weekRange,
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    onTodayClick = onTodayClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = MAX_CONTENT_WIDTH)
                        .align(Alignment.CenterHorizontally)
                )
            }

            if (uiState.viewMode == AgendaViewMode.DAILY && uiState.totalCount > 0) {
                AgendaProgressBar(
                    completedCount = uiState.completedCount,
                    totalCount = uiState.totalCount,
                    progress = uiState.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = MAX_CONTENT_WIDTH)
                        .align(Alignment.CenterHorizontally)
                )
            }

            val contentModifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .widthIn(max = MAX_CONTENT_WIDTH)
                .align(Alignment.CenterHorizontally)

            when {
                uiState.isLoading -> AgendaLoadingIndicator(modifier = contentModifier)

                uiState.isEmpty -> AgendaEmptyState(
                    message = when (uiState.viewMode) {
                        AgendaViewMode.DAILY -> stringResource(R.string.agenda_empty_daily_state)
                        AgendaViewMode.WEEKLY -> stringResource(R.string.agenda_empty_weekly_state)
                        AgendaViewMode.ALL -> stringResource(R.string.agenda_empty_all_state)
                    },
                    ctaText = stringResource(R.string.task_add),
                    onAddTaskClick = onAddTaskClick,
                    modifier = contentModifier
                )

                uiState.viewMode == AgendaViewMode.DAILY -> DailyAgendaList(
                    tasks = uiState.dailyTasks,
                    onTaskClick = onTaskClick,
                    onToggleComplete = onToggleComplete,
                    modifier = contentModifier
                )

                uiState.viewMode == AgendaViewMode.ALL -> DailyAgendaList(
                    tasks = uiState.allTasks,
                    onTaskClick = onTaskClick,
                    onToggleComplete = onToggleComplete,
                    modifier = contentModifier
                )

                else -> WeeklyAgendaList(
                    tasksByDay = uiState.weeklyTasks,
                    onTaskClick = onTaskClick,
                    onToggleComplete = onToggleComplete,
                    modifier = contentModifier
                )
            }

            BannerAdView(
                adUnitId = stringResource(R.string.admob_banner_agenda_id),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}