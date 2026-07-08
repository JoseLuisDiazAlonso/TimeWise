package com.timewise.app.ui.agenda.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.timewise.app.domain.model.Priority
import com.timewise.app.domain.model.Task
import com.timewise.app.ui.theme.PriorityHigh
import com.timewise.app.ui.theme.PriorityLow
import com.timewise.app.ui.theme.PriorityMedium
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.annotation.meta.When

/**Este archivo representa la lógica de una fila individual de la agenda.
 * Es un composable, es decir, recibe datos y un callback de click y no
 * conoce el ViewModel. Esto lo hace reutilizable.
 * */

@Composable
fun TaskAgendaItem (
    task: Task,
    onClick: (Task) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(task) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)

    ) {
        Row (
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically

        )  {
            //Barra de color lateral que indica la prioridad de una tarea de un vistazo
            Box (
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .background(
                        color = task.priority.toColor (),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
            Spacer (modifier = Modifier.width(12.dp))

            Column (modifier = Modifier.weight(1f)){
                Text (
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted)  {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                if (task.description.isNotBlank()) {
                    Text (
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1
                    )
                }
            }
            task.dueDate?.let { dueDate ->
                Text (
                    text = dueDate.toHourMinute(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }

}

private fun Priority.toColor(): Color = when (this) {
    Priority.HIGH -> PriorityHigh
    Priority.MEDIUM -> PriorityMedium
    Priority.LOW -> PriorityLow
}

private fun Long.toHourMinute(): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}
