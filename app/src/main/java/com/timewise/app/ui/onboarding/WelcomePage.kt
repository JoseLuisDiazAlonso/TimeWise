package com.timewise.app.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.timewise.app.R

/*
Este archivo determina la vista de la página de bienvenida*
*
*/
@Composable
fun WelcomePage(step: Int) {
    val (titleRes, descRes) = when (step) {
        0-> R.string.onboarding_step1_title to R.string.onboarding_step1_desc
        1-> R.string.onboarding_step2_title to R.string.onboarding_step2_desc
        2-> R.string.onboarding_step3_title to R.string.onboarding_step3_desc
        else -> R.string.onboarding_step4_title to R.string.onboarding_step4_desc
    }

    Column (
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(id = titleRes), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(id = descRes), style = MaterialTheme.typography.bodyLarge
        , textAlign = TextAlign.Center)
    }


}
