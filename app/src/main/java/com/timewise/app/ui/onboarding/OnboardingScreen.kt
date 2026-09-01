package com.timewise.app.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.timewise.app.ui.common.MAX_CONTENT_WIDTH
import kotlinx.coroutines.launch

private const val TOTAL_PAGES = 6

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { TOTAL_PAGES })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> LanguageSelectorPage()
                1, 2, 3 -> WelcomePage(step = page - 1)
                4 -> NotificationPermissionPage()
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
            },
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = MAX_CONTENT_WIDTH)
        )
    }
}