package com.timewise.app.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Ancho máximo compartido por toda la app para que ninguna pantalla se estire
 * de forma absurda en tablet, sobre todo en landscape.
 */
val MAX_CONTENT_WIDTH = 600.dp

/**
 * Para pantallas de CONTENIDO FIJO (formularios, pantallas con pocos elementos,
 * como la de Exportación). Añade scroll vertical (por si el contenido no cabe
 * en landscape con poca altura) y centra todo con un ancho máximo en tablet.
 *
 * Uso: envuelve el Column/contenido de tu pantalla con esto en vez de un
 * Scaffold -> Column suelto.
 */
@Composable
fun ResponsiveScrollableScreen(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = MAX_CONTENT_WIDTH),
                content = content
            )
        }
    )
}

/**
 * Para pantallas con LISTAS (LazyColumn/LazyRow), como Time-blocking o
 * Estadísticas. NO añade verticalScroll (la propia lista ya lo tiene), solo
 * limita y centra el ancho horizontal en tablet.
 *
 * Uso: aplica este Modifier directamente a tu LazyColumn/LazyRow existente,
 * sin envolver nada más.
 */
fun Modifier.responsiveMaxWidth(): Modifier =
    this
        .fillMaxWidth()
        .widthIn(max = MAX_CONTENT_WIDTH)