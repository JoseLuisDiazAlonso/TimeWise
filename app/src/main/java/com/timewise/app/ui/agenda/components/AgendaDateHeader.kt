package com.timewise.app.ui.agenda.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import com.timewise.app.ui.agenda.AgendaViewMode
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun AgendaDateHeader(
    viewMode: AgendaViewMode,
    selectedDate: LocalDate,
    weekRange: Pair<LocalDate, LocalDate>?,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onTodayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_agenda_previous)
            )
        }

        Text(
            text = formatDateLabel(viewMode, selectedDate, weekRange),
            style = MaterialTheme.typography.titleMedium
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onTodayClick) {
                Text(text = stringResource(R.string.today))
            }
            IconButton(onClick = onNextClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.cd_agenda_next)
                )
            }
        }
    }
}

private fun formatDateLabel(
    viewMode: AgendaViewMode,
    selectedDate: LocalDate,
    weekRange: Pair<LocalDate, LocalDate>?
): String {
    val locale = Locale.getDefault()

    return when (viewMode) {
        AgendaViewMode.DAILY -> {
            val dayName = selectedDate.dayOfWeek
                .getDisplayName(TextStyle.FULL, locale)
                .replaceFirstChar { it.uppercase() }
            val monthName = selectedDate.month.getDisplayName(TextStyle.FULL, locale)
            "$dayName, ${selectedDate.dayOfMonth} $monthName"
        }

        AgendaViewMode.WEEKLY -> {
            val (start, end) = weekRange ?: (selectedDate to selectedDate)
            val startMonth = start.month.getDisplayName(TextStyle.SHORT, locale)
            val endMonth = end.month.getDisplayName(TextStyle.SHORT, locale)

            if (start.month == end.month) {
                "${start.dayOfMonth} - ${end.dayOfMonth} $startMonth"
            } else {
                "${start.dayOfMonth} $startMonth - ${end.dayOfMonth} $endMonth"
            }
        }
    }
}