package com.timewise.app.ui.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timewise.app.R
import com.timewise.app.domain.model.CategoryTimeStats
import com.timewise.app.domain.model.TimeStatsPeriod
import com.timewise.app.ui.common.ResponsiveScrollableScreen

@Composable
fun StatisticsScreen(
    onUpgradeClick: () -> Unit = {},
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (!uiState.isLoading && !uiState.isUnlocked) {
        StatisticsLockedContent(onUpgradeClick = onUpgradeClick)
    } else {
        StatisticsContent(
            uiState = uiState,
            onPeriodSelected = viewModel::onPeriodSelected
        )
    }
}

@Composable
private fun StatisticsLockedContent(onUpgradeClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.stats_premium_locked),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onUpgradeClick) {
                Text(stringResource(R.string.premium_cta))
            }
        }
    }
}

@Composable
private fun StatisticsContent(
    uiState: StatisticsUiState,
    onPeriodSelected: (TimeStatsPeriod) -> Unit
) {
    ResponsiveScrollableScreen {
        Column(modifier = Modifier.padding(16.dp)) {
            val selectedIndex = if (uiState.period == TimeStatsPeriod.SEMANAL) 0 else 1
            TabRow(selectedTabIndex = selectedIndex) {
                Tab(
                    selected = selectedIndex == 0,
                    onClick = { onPeriodSelected(TimeStatsPeriod.SEMANAL) },
                    text = { Text(stringResource(R.string.stats_tab_weekly)) }
                )
                Tab(
                    selected = selectedIndex == 1,
                    onClick = { onPeriodSelected(TimeStatsPeriod.MENSUAL) },
                    text = { Text(stringResource(R.string.stats_tab_monthly)) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (uiState.isEmpty) {
                Text(
                    stringResource(R.string.stats_no_data),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                Text(
                    stringResource(R.string.stats_category_breakdown_title),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))

                uiState.categoryStats.forEach { stat ->
                    CategoryRow(stat = stat)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(stat: CategoryTimeStats) {
    val category = com.timewise.app.ui.timeblocking.categoryOptionForHex(stat.categoryColorHex)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.width(12.dp).height(12.dp),
                shape = RoundedCornerShape(3.dp),
                color = runCatching { Color(android.graphics.Color.parseColor(stat.categoryColorHex)) }
                    .getOrDefault(MaterialTheme.colorScheme.primary)
            ) {}
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(category.labelRes), style = MaterialTheme.typography.bodyMedium)
        }
        Text("${(stat.percentage * 100).toInt()}%", style = MaterialTheme.typography.bodyMedium)
    }
}