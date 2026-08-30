package com.timewise.app.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.timewise.app.ui.common.MAX_CONTENT_WIDTH
import kotlinx.coroutines.launch

/*
Esta clase se utilizará para en lugar de crear 4 rutas de navegación diferentes crear
una sola ruta de navegación con 7 páginas internas
*/

private const val TOTAL_PAGES = 7

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { TOTAL_PAGES })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        // El Pager se deja a ancho completo (sin widthIn): cada página interna
        // (WelcomePage, PlansPage...) puede tener su propia imagen o fondo que
        // sí conviene que ocupe todo el ancho en tablet. El límite de ancho
        // para el TEXTO/botones de cada slide se aplica dentro de esas páginas,
        // no aquí en el contenedor.
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0, 1, 2, 3 -> WelcomePage(step = page)
                4 -> LanguageSelectorPage()
                5 -> NotificationPermissionPage()
                else -> PlansPage()
            }
        }

        // La barra de navegación (indicador de página + botones) sí se limita
        // y centra: en tablet ancha, unos botones de "Siguiente/Saltar" a todo
        // el ancho de la pantalla se verían desproporcionados.
        OnboardingBottomBar(
            currentPage = pagerState.currentPage,
            totalPages = TOTAL_PAGES,
            onSkipClick = { viewModel.onboardingFinished(onFinished) },
            onNextClick = {
                scope.launch {
                    if (pagerState.currentPage == TOTAL_PAGES - 1) {
                        viewModel.onboardingFinished(onFinished)
                    } else {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MAX_CONTENT_WIDTH)
        )
    }
}