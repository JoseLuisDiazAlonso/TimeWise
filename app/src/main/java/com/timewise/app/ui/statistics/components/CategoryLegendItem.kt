package com.timewise.app.ui.statistics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.timewise.app.domain.model.CategoryTimeStats
import androidx.compose.foundation.layout.fillMaxWidth

/**
 * Fila de leyenda de una categoría dentro de las estadísticas: círculo de color,
 * nombre de la categoría, tiempo total y porcentaje sobre el periodo.
 **/

@Composable
fun CategoryLegendItem(
    stat: CategoryTimeStats,
    categoryName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Swatch: usa el colorHex crudo, no un token de MaterialTheme (sección 10 de la guía)
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = Color(android.graphics.Color.parseColor(stat.categoryColorHex)),
                    shape = CircleShape
                )
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = categoryName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${stat.totalMinutes} min",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "${(stat.percentage * 100).toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}