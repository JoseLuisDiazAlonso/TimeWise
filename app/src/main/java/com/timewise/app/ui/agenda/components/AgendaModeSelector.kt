package com.timewise.app.ui.agenda.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.timewise.app.R
import com.timewise.app.ui.agenda.AgendaViewMode

@Composable
fun AgendaModeSelector(
    selectedMode: AgendaViewMode,
    onModeSelected: (AgendaViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    // El enum ya está en el mismo orden que los tabs (DAILY, WEEKLY, ALL),
    // así que el ordinal sirve directamente como índice seleccionado.
    TabRow(
        selectedTabIndex = selectedMode.ordinal,
        modifier = modifier
    ) {
        Tab(
            selected = selectedMode == AgendaViewMode.DAILY,
            onClick = { onModeSelected(AgendaViewMode.DAILY) },
            text = { Text(stringResource(R.string.agenda_mode_daily)) }
        )
        Tab(
            selected = selectedMode == AgendaViewMode.WEEKLY,
            onClick = { onModeSelected(AgendaViewMode.WEEKLY) },
            text = { Text(stringResource(R.string.agenda_mode_weekly)) }
        )
        Tab(
            selected = selectedMode == AgendaViewMode.ALL,
            onClick = { onModeSelected(AgendaViewMode.ALL) },
            text = { Text(stringResource(R.string.agenda_mode_all)) }
        )
    }
}