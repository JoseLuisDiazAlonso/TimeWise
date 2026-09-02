package com.timewise.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.timewise.app.R

/*
Este archivo determina la vista de la página de bienvenida.
Solo existen 3 steps reales (0, 1, 2): la antigua slide de sincronización
en la nube (step 3) se eliminó al pasar de Firebase a Android Auto Backup,
así que el "else" ya no necesita cubrir ese caso -> cubre el step 2.
*/
@Composable
fun WelcomePage(step: Int) {
    val (titleRes, descRes) = when (step) {
        0 -> R.string.onboarding_step1_title to R.string.onboarding_step1_desc
        1 -> R.string.onboarding_step2_title to R.string.onboarding_step2_desc
        else -> R.string.onboarding_step3_title to R.string.onboarding_step3_desc
    }

    val iconRes = when (step) {
        0 -> R.drawable.ic_onboarding_organize
        1 -> R.drawable.ic_onboarding_timeblocks
        else -> R.drawable.ic_onboarding_reminders
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                // Unspecified respeta los colores propios definidos dentro de cada
                // vector (los alphas de la diana, las categorías del calendario...).
                // Con un tint normal, Compose pintaría todo el icono de un solo color.
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(stringResource(id = titleRes), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(id = descRes),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}