package com.timewise.app.ui.agenda.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.timewise.app.R

/**
 * @Composable reutilizable en cualquier pantalla que quiera ofrecer un desbloqueo
 * temporal sin repetir lógica.
 *
 * Si isUnlocked es true, muestra una insignia "Desbloqueado · Xh".
 * Si es false, muestra un botón con icono de vídeo que invoca onWatchAdClicked.
 */
@Composable
fun RewardedUnlockButton(
    isUnlocked: Boolean,
    remainingHours: Int,
    onWatchAdClicked: () -> Unit
) {
    if (isUnlocked) {
        AssistChip(
            onClick = {},
            enabled = false,
            label = {
                Text(text = stringResource(R.string.ads_rewarded_unlocked_badge, remainingHours))
            }
        )
    } else {
        Button(onClick = onWatchAdClicked) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = stringResource(R.string.ads_rewarded_cd_icon)
            )
            Text(text = stringResource(R.string.ads_rewarded_watch_button))
        }
    }
}