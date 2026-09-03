package com.timewise.app.ui.timeblocking.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.timewise.app.ui.timeblocking.TimeBlockUiModel
import com.timewise.app.ui.timeblocking.TimeCalculationUtils

/**
 * Ya no es arrastrable: toda la edición de horario se hace tocando el bloque, que
 * abre el formulario con selectores de hora de inicio/fin. Sustituye al gesto de
 * arrastre anterior, más frágil en dispositivos reales.
 */
@Composable
fun TimeBlockItem(
    block: TimeBlockUiModel,
    onClick: (TimeBlockUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val topDp = TimeCalculationUtils.minutesToDp(block.offsetMinutes)
    val heightDp = TimeCalculationUtils.minutesToDp(block.durationMinutes)

    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .offset { IntOffset(x = 0, y = topDp.roundToPx()) }
            .height(heightDp)
            .pointerInput(block.id) {
                detectTapGestures(onTap = { onClick(block) })
            }
            .clip(RoundedCornerShape(8.dp))
            .background(
                Color(AndroidColor.parseColor(block.categoryColor))
                    .copy(alpha = if (block.isCompleted) 0.55f else 1f)
            )
    ) {
        Text(
            text = block.title,
            color = Color.White,
            modifier = Modifier.padding(6.dp)
        )
    }
}