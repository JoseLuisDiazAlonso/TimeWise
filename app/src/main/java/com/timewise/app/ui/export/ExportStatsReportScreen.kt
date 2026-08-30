package com.timewise.app.ui.export

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timewise.app.R
import com.timewise.app.ui.common.ResponsiveScrollableScreen

/**
 * Composable que lanza el selector SAF, filtra el caso 'cancelado' (uri null) sin tocar
 * el ViewModel, y reacciona a ExportUiState con Snackbar o navegación al paywall.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportStatsReportScreen(
    viewModel: ExportStatsReportViewModel = hiltViewModel(),
    onNavigateToPaywall: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val exportSuccessMessage = stringResource(R.string.export_success)
    val exportErrorMessage = stringResource(R.string.export_error)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri: Uri? ->
        // Cancelación silenciosa: si uri es null, no se llama al ViewModel.
        if (uri != null) viewModel.onExportRequested(uri)
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is ExportUiState.Success -> {
                snackbarHostState.showSnackbar(exportSuccessMessage)
                viewModel.consumeState() // vuelve a Idle, sin tocar el UseCase
            }
            is ExportUiState.Error -> {
                snackbarHostState.showSnackbar(exportErrorMessage)
                viewModel.consumeState()
            }
            is ExportUiState.PremiumRequired -> {
                onNavigateToPaywall()
                viewModel.consumeState()
            }
            // Idle y Loading no disparan ninguna acción -> Unit, nunca TODO()
            ExportUiState.Idle -> Unit
            ExportUiState.Loading -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        // ResponsiveScrollableScreen sustituye al Column suelto de nivel superior:
        // esta pantalla es de contenido fijo (un único botón), no una lista, así
        // que aquí SÍ corresponde el wrapper con scroll + ancho máximo centrado
        // (el mismo patrón ya aplicado en TaskFormScreen).
        ResponsiveScrollableScreen(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { launcher.launch("TimeWise_Informe_Semanal.pdf") },
                    enabled = uiState !is ExportUiState.Loading
                ) {
                    if (uiState is ExportUiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    } else {
                        Text(stringResource(R.string.export_button))
                    }
                }
            }
        }
    }
}