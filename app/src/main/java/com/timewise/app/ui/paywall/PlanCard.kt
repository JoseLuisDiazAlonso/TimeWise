package com.timewise.app.ui.paywall

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R
import com.timewise.app.domain.model.SubscriptionPeriod
import com.timewise.app.domain.model.SubscriptionPlan

@Composable
fun PlanCard(
    plan: SubscriptionPlan,
    onClick: () -> Unit
) {
    val isAnnual = plan.period == SubscriptionPeriod.ANNUAL
    val labelRes = if (isAnnual) {
        R.string.paywall_plan_annual_label
    } else {
        R.string.paywall_plan_monthly_label
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = if (isAnnual) BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = plan.formattedPrice,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50)
            ) {
                Text(text = stringResource(R.string.paywall_cta_subscribe))
            }
        }
    }
}