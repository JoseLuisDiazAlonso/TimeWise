package com.timewise.app.ui.timeblocking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.timewise.app.ui.timeblocking.components.DailyTimeGrid
import com.timewise.app.ui.timeblocking.components.DraggableTimeBlock
import kotlinx.coroutines.launch

/**
 * Pantalla raíz del Card #17. Recoge TimeBlockingViewModel con hiltViewModel(), colecciona
 * uiState.collectAsStateWithLifecycle() y compone DailyTimeGrid + una capa de
 * DraggableTimeBlock por cada elemento de timeBlocks, dentro de un Box con scroll vertical.
 * Si isPremiumUnlocked es false, muestra el paywall del Card #16 en su lugar.
 *
 * Funciones:
 *  - fun TimeBlockingScreen(navController: NavController,
 *    viewModel: TimeBlockingViewModel = hiltViewModel()): Unit
 **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeBlockingScreen(
    navController: NavController,
    viewModel: TimeBlockingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showForm by remember { mutableStateOf(false) }
    var editingBlock by remember { mutableStateOf<TimeBlockUiModel?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            coroutineScope.launch { snackbarHostState.showSnackbar(message) }
            viewModel.onErrorMessageShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Planificación del día") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (uiState.isPremiumUnlocked) {
                FloatingActionButton(onClick = {
                    editingBlock = null
                    showForm = true
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir bloque")
                }
            }
        }
    ) { paddingValues ->
        if (!uiState.isPremiumUnlocked) {
            // TODO: sustituir por el composable de paywall real del Card #16
            // (confirmar nombre exacto: p. ej. PremiumPaywallContent(...))
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                Text(
                    text = "Función Premium — desbloquea la planificación visual con TimeWise Premium",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
            ) {
                DateSelector(
                    selectedDate = uiState.selectedDate,
                    onPreviousDay = { viewModel.onDateSelected(uiState.selectedDate.minusDays(1)) },
                    onNextDay = { viewModel.onDateSelected(uiState.selectedDate.plusDays(1)) }
                )

                Box(modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                ) {
                    DailyTimeGrid(modifier = Modifier.fillMaxSize())

                    uiState.timeBlocks.forEach { block ->
                        DraggableTimeBlock(
                            block = block,
                            onDragEnd = { id, newOffsetMinutes, newDurationEndMinutes ->
                                viewModel.onBlockDragEnd(id, newOffsetMinutes, newDurationEndMinutes)
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
            onConfirm = { title: String, start: Int, end: Int, color: String ->
                if (editingBlock == null) {
                    viewModel.onCreateBlock(title, start, end, color)
                } else {
                    // TODO: viewModel.onUpdateBlock(...) — pendiente de crear en el ViewModel
                }
                showForm = false
            }
        )
    }
}

@Composable
fun TimeBlockFormBottomSheet(
    initialBlock: TimeBlockUiModel?,
    onDismiss: () -> Unit,
    onConfirm: Any
) {
    TODO("Not yet implemented")
}

@Composable
private fun DateSelector(
    selectedDate: java.time.LocalDate,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit
) {
    Row(modifier = Modifier.padding(8.dp)) {
        IconButton(onClick = onPreviousDay) {
            Icon(Icons.Filled.ChevronLeft, contentDescription = "Día anterior")
        }
        Text(
            text = selectedDate.toString(),
            modifier = Modifier.padding(top = 12.dp)
        )
        IconButton(onClick = onNextDay) {
            Icon(Icons.Filled.ChevronRight, contentDescription = "Día siguiente")
        }
    }
}