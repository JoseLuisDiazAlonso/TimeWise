package com.timewise.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.timewise.app.R

/*
Este archivo va a controlar los botones de next y skipt de la página de Onboarding
*
**/

@Composable
fun OnboardingBottomBar(
    currentPage: Int,
    totalPages: Int,
    onSkipClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val isLastPage = currentPage == totalPages - 1
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onSkipClick, enabled = !isLastPage) {
            Text(if (isLastPage) "" else stringResource(R.string.onboarding_skip))
        }
        PageIndicator (currentPage = currentPage, totalPages = totalPages)
        Button(onClick = onNextClick) {
            Text(
                stringResource(
                    if (isLastPage) R.string.onboarding_start else R.string.onboarding_next
                )
            )
        }
    }
}
@Composable
private fun PageIndicator (
    currentPage: Int,
    totalPages: Int
) {
    Row {
        repeat(totalPages) { index ->
            val active = index == currentPage
            Box (
                modifier = Modifier
                    .padding (horizontal = 3.dp)
                    .size (if (active) 9.dp else 7.dp)
                    .clip (shape = CircleShape)
                    .background (
                        if (active) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f)
                    )
            )

        }
    }

}
