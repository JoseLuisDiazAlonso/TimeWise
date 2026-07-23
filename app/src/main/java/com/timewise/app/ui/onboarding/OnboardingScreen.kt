package com.timewise.app.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
            }
        )
    }
}