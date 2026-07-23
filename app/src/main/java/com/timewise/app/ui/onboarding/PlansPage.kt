package com.timewise.app.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import com.timewise.app.ui.theme.TimeWisePrimary

/*
Este archivo configura la página que muestra los diferentes tipos de planes
(Free vs Premium) dentro del flujo de Onboarding.
*/

@Composable
fun PlansPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.onboarding_plans_title), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        PlanCard(
            title = stringResource(R.string.plan_free_title),
            price = stringResource(R.string.plan_free_price),
            features = listOf(
                stringResource(R.string.plan_free_feature_1),
                stringResource(R.string.plan_free_feature_2),
                stringResource(R.string.plan_free_feature_3)
            ),
            highlighted = false
        )
        Spacer(modifier = Modifier.height(16.dp))

        PlanCard(
            title = stringResource(R.string.plan_premium_title),
            price = stringResource(R.string.plan_premium_price),
            features = listOf(
                stringResource(R.string.plan_premium_feature_1),
                stringResource(R.string.plan_premium_feature_2),
                stringResource(R.string.plan_premium_feature_3),
                stringResource(R.string.plan_premium_feature_4)
            ),
            highlighted = true
        )
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    features: List<String>,
    highlighted: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (highlighted) TimeWisePrimary.copy(alpha = 0.08f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (highlighted) BorderStroke(1.5.dp, TimeWisePrimary) else null
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(price, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(12.dp))
            features.forEach { feature ->
                Text("• $feature", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}