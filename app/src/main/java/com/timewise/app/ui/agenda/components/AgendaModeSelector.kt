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
    val selectedIndex = if (selectedMode == AgendaViewMode.DAILY) 0 else 1

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
    ) {
        Tab(
            selected = selectedIndex == 0,
            onClick = { onModeSelected(AgendaViewMode.DAILY) },
            text = { Text(stringResource(R.string.agenda_mode_daily)) }
        )
        Tab(
            selected = selectedIndex == 1,
            onClick = { onModeSelected(AgendaViewMode.WEEKLY) },
            text = { Text(stringResource(R.string.agenda_mode_weekly)) }
        )
    }
}