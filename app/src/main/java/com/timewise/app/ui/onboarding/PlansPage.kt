package com.timewise.app.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
            annualPrice = stringResource(R.string.plan_premium_price_annual),
            savingsLabel = stringResource(R.string.plan_premium_savings_badge),
            features = listOf(
                stringResource(R.string.plan_premium_feature_5),
                stringResource(R.string.plan_premium_feature_2),
                stringResource(R.string.plan_premium_feature_3),
                stringResource(R.string.plan_premium_feature_4),
                stringResource(R.string.plan_premium_feature_1)
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
    highlighted: Boolean,
    annualPrice: String? = null,
    savingsLabel: String? = null
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

            // Si hay precio anual, se muestran ambos lado a lado (mensual / anual)
            // con la insignia de ahorro. Si no (plan Free), se mantiene el precio
            // único tal como estaba antes.
            if (annualPrice != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    PriceOption(
                        label = stringResource(R.string.plan_price_monthly_label),
                        price = price,
                        highlighted = false,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    PriceOption(
                        label = stringResource(R.string.plan_price_annual_label),
                        price = annualPrice,
                        highlighted = true,
                        savingsLabel = savingsLabel,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Text(price, style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(12.dp))
            features.forEach { feature ->
                Text("• $feature", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun PriceOption(
    label: String,
    price: String,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
    savingsLabel: String? = null
) {
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = if (highlighted) TimeWisePrimary else MaterialTheme.colorScheme.surface,
            border = if (!highlighted) BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null
        ) {
            Column(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (highlighted) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    price,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (highlighted) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Insignia "Ahorra X%" anclada en la esquina superior de la opción anual,
        // igual que en el mockup del Paywall.
        if (savingsLabel != null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-10).dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.tertiary
            ) {
                Text(
                    text = savingsLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}