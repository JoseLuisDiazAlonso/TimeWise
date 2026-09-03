package com.timewise.app.ui.timeblocking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timewise.app.R
import com.timewise.app.ui.timeblocking.components.DailyTimeGrid

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeBlockingScreen(
    onUpgradeClick: () -> Unit = {},
    viewModel: TimeBlockingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showForm by remember { mutableStateOf(false) }
    var editingBlock by remember { mutableStateOf<TimeBlockUiModel?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val errorText = uiState.errorMessageRes?.let { stringResource(it) }
    LaunchedEffect(errorText) {
        errorText?.let { message ->
            coroutineScope.launch { snackbarHostState.showSnackbar(message) }
            viewModel.onErrorMessageShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.time_blocking_title)) },
                actions = {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "PREMIUM",
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(end = 8.dp))
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (uiState.isPremiumUnlocked) {
                FloatingActionButton(onClick = {
                    editingBlock = null
                    showForm = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.time_blocking_add_block))
                }
            }
        }
    ) { paddingValues ->
        if (!uiState.isLoading && !uiState.isPremiumUnlocked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.time_blocking_premium_message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onUpgradeClick) {
                        Text(stringResource(R.string.premium_cta))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                DateSelector(
                    selectedDate = uiState.selectedDate,
                    onPreviousDay = { viewModel.onDateSelected(uiState.selectedDate.minusDays(1)) },
                    onNextDay = { viewModel.onDateSelected(uiState.selectedDate.plusDays(1)) }
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    DailyTimeGrid(modifier = Modifier.fillMaxSize())

                    uiState.timeBlocks.forEach { block ->
                        com.timewise.app.ui.timeblocking.components.TimeBlockItem(
                            block = block,
                            onClick = {
                                editingBlock = it
                                showForm = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showForm) {
        TimeBlockFormBottomSheet(
            initialBlock = editingBlock,
            onDismiss = { showForm = false },
            onConfirm = { title, start, end, colorHex ->
                val current = editingBlock
                if (current == null) {
                    viewModel.onCreateBlock(title, start, end, colorHex)
                } else {
                    viewModel.onUpdateBlock(current.id, title, start, end, colorHex)
                }
                showForm = false
            },
            onDelete = {
                editingBlock?.let { viewModel.onDeleteBlock(it.id) }
                showForm = false
            }
        )
    }
}

@Composable
private fun DateSelector(
    selectedDate: java.time.LocalDate,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit
) {
    Row(modifier = Modifier.padding(8.dp)) {
        IconButton(onClick = onPreviousDay) {
            Icon(Icons.Filled.ChevronLeft, contentDescription = null)
        }
        Text(
            text = selectedDate.toString(),
            modifier = Modifier.padding(top = 12.dp)
        )
        IconButton(onClick = onNextDay) {
            Icon(Icons.Filled.ChevronRight, contentDescription = null)
        }
    }
}