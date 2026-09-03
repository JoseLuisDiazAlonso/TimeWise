package com.timewise.app.ui.agenda.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.timewise.app.domain.model.Task

@Composable
fun DailyAgendaList(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit,
    onToggleComplete: (Task) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Adaptive: en móvil vertical no hay sitio para más de una columna de 280dp,
    // así que se comporta igual que antes (una sola columna). En horizontal (o
    // tablet), al haber más ancho disponible, aparecen 2+ columnas automáticamente,
    // aprovechando ese espacio en vez de forzar todo en una lista larga.
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 280.dp),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = tasks, key = { it.id }) { task ->
            TaskAgendaItem(task = task, onClick = onTaskClick, onToggleComplete = onToggleComplete)
        }
    }
}