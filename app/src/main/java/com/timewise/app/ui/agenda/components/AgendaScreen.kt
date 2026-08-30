package com.timewise.app.ui.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
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
import com.timewise.app.ui.agenda.components.DailyAgendaList
import com.timewise.app.ui.agenda.components.WeeklyAgendaList
import com.timewise.app.ui.agenda.components.ads.BannerAdView

// Mismo límite que usas en el resto de la app para que ninguna pantalla se
// estire de forma absurda en tablet, sobre todo en landscape.
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
        onAddTaskClick = onAddTaskClick
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
    onAddTaskClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        // Controles fijos de arriba: limitados a un ancho máximo y centrados
        // en tablet, sin cambiar su comportamiento en móvil (ahí fillMaxWidth
        // ya es menor que 600dp, así que widthIn no hace nada).
        AgendaModeSelector(
            selectedMode = uiState.viewMode,
            onModeSelected = onModeSelected,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MAX_CONTENT_WIDTH)
                .align(Alignment.CenterHorizontally)
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
            onTodayClick = onTodayClick,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MAX_CONTENT_WIDTH)
                .align(Alignment.CenterHorizontally)
        )

        // FIX del bug: antes era fillMaxSize() -> ocupaba TODO el alto restante,
        // dejando 0 espacio para el BannerAdView de debajo (o empujándolo fuera
        // de pantalla). Con weight(1f), este bloque ocupa el espacio que sobra
        // DESPUÉS de reservar sitio para el banner, no todo el espacio posible.
        //
        // El .widthIn(max = ...) + .align(CenterHorizontally) es el mismo ajuste
        // de tablet que en los controles de arriba. Como DailyAgendaList y
        // WeeklyAgendaList ya son LazyColumn por dentro, NO se les añade
        // verticalScroll (causaría el crash de scroll anidado).
        val contentModifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .widthIn(max = MAX_CONTENT_WIDTH)
            .align(Alignment.CenterHorizontally)

        when {
            uiState.isLoading -> AgendaLoadingIndicator(modifier = contentModifier)

            uiState.isEmpty -> AgendaEmptyState(
                message = if (uiState.viewMode == AgendaViewMode.DAILY) {
                    stringResource(R.string.agenda_empty_daily_state)
                } else {
                    stringResource(R.string.agenda_empty_weekly_state)
                },
                ctaText = stringResource(R.string.task_add),
                onAddTaskClick = onAddTaskClick,
                modifier = contentModifier
            )

            uiState.viewMode == AgendaViewMode.DAILY -> DailyAgendaList(
                tasks = uiState.dailyTasks,
                onTaskClick = onTaskClick,
                modifier = contentModifier
            )

            else -> WeeklyAgendaList(
                tasksByDay = uiState.weeklyTasks,
                onTaskClick = onTaskClick,
                modifier = contentModifier
            )
        }

        // El banner de AdMob se deja a ancho completo (fillMaxWidth, sin
        // widthIn): los banners adaptativos de AdMob calculan su propio ancho
        // en función del dispositivo, así que restringirlo aquí podría romper
        // ese cálculo. Ahora sí tiene espacio real para dibujarse, porque el
        // bloque de arriba ya no se come todo el alto con fillMaxSize().
        BannerAdView(
            adUnitId = stringResource(R.string.admob_banner_agenda_id),
            modifier = Modifier.fillMaxWidth()
        )
    }
}