package com.timewise.app.ui.timeblocking.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.timewise.app.ui.timeblocking.TimeGridConstants

/**
 * Composable.
 * Dibuja el fondo de la rejilla horaria: 24 filas de altura TimeGridConstants.HOUR_HEIGHT_DP.dp
 * con una línea divisoria y la etiqueta de la hora a la izquierda.
 *
 * Funciones:
 *  - fun DailyTimeGrid(modifier: Modifier = Modifier): Unit. Dibuja la rejilla horaria.
 **/
@Composable
fun DailyTimeGrid(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        for (hour in TimeGridConstants.START_HOUR until TimeGridConstants.END_HOUR) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(TimeGridConstants.HOUR_HEIGHT_DP.dp),
                verticalAlignment = Alignment.Top
            ) {
                TimeLabel(hour = hour)
                TimeDivider(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TimeLabel(hour: Int) {
    Text(
        text = "%02d:00".format(hour),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .width(40.dp)
            .padding(top = 2.dp, end = 4.dp)
    )
}

@Composable
private fun TimeDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = 1.dp,
        color = Color(0xFFC5CAE9) // token "Línea de hora" de la sección de estética del Card #17
    )
}