package com.timewise.app.ui.timeblocking.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.timewise.app.ui.timeblocking.TimeBlockUiModel
import com.timewise.app.ui.timeblocking.TimeCalculationUtils
import kotlin.math.roundToInt

/**
 * Representa un único bloque sobre la rejilla y gestiona su gesto de arrastre vertical con
 * Modifier.pointerInput(blockId) { detectDragGestures(...) }. Mientras se arrastra, acumula el
 * delta en un valor local y solo notifica al ViewModel en onDragEnd, no en cada frame.
 *
 * Funciones:
 *  - fun DraggableTimeBlock(block: TimeBlockUiModel, onDragEnd: (Long, Int, Int) -> Unit,
 *  modifier: Modifier = Modifier): Unit
 **/
@Composable
fun DraggableTimeBlock(
    block: TimeBlockUiModel,
    onDragEnd: (Long, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var dragOffsetPx by remember(block.id) { mutableStateOf(0f) }
    val density = LocalDensity.current

    val topDp = TimeCalculationUtils.minutesToDp(block.offsetMinutes)
    val heightDp = TimeCalculationUtils.minutesToDp(block.durationMinutes)

    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .height(heightDp)
            .offset {
                IntOffset(x = 0, y = topDp.roundToPx() + dragOffsetPx.roundToInt())
            }
            .pointerInput(block.id) {
                detectDragGestures(
                    onDragEnd = {
                        val movedMinutes = with(density) {
                            TimeCalculationUtils.dpToMinutes(dragOffsetPx.toDp())
                        }
                        val newOffsetMinutes = TimeCalculationUtils.snapToGrid(
                            block.offsetMinutes + movedMinutes
                        )
                        onDragEnd(block.id, newOffsetMinutes, newOffsetMinutes + block.durationMinutes)
                        dragOffsetPx = 0f
                    },
                    onDragCancel = { dragOffsetPx = 0f }
                ) { change, dragAmount ->
                    change.consume()
                    dragOffsetPx += dragAmount.y
                }
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