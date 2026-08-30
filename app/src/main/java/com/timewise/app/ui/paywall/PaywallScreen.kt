package com.timewise.app.ui.paywall

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.timewise.app.R
import com.timewise.app.domain.model.PurchaseState
import com.timewise.app.ui.common.ResponsiveScrollableScreen

/**
 * Será un composable que presentará los dos planes, permite elegir uno y
 * muestra el estado de la compra.
 *
 * Debe de ser accesible desde cualquier punto donde el usuario tope con el límite freemium.
 */
@Composable
fun PaywallScreen(
    navController: NavController,
    viewModel: PaywallViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current
    val plans by viewModel.plans.collectAsStateWithLifecycle()
    val purchaseState by viewModel.purchaseState.collectAsStateWithLifecycle()

    // Al completarse la compra nos lleva a la pantalla anterior
    LaunchedEffect(purchaseState) {
        if (purchaseState is PurchaseState.Purchased) {
            navController.popBackStack()
        }
    }

    // ResponsiveScrollableScreen sustituye al Column suelto de nivel superior:
    // el número de planes es variable (hoy 2, mañana podrían ser 4), así que
    // el scroll es imprescindible aquí -> sin él, en landscape con poca altura
    // los planes de más abajo (o el botón de compra) podrían quedar cortados
    // e inalcanzables, justo en la pantalla donde eso más duele (conversión).
    ResponsiveScrollableScreen {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.paywall_title),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(16.dp))
            plans.forEach { plan ->
                PlanCard(plan = plan) {
                    if (activity != null) {
                        viewModel.onPlanSelected(activity, plan)
                    }
                }
            }
            when (purchaseState) {
                is PurchaseState.Pending -> CircularProgressIndicator()
                is PurchaseState.Error -> Text(
                    text = stringResource(R.string.billing_error_generic),
                    color = MaterialTheme.colorScheme.error
                )
                else -> Unit
            }
        }
    }
}