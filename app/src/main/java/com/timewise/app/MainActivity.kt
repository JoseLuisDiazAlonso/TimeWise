package com.timewise.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.timewise.app.data.local.locale.LocaleHelper
import com.timewise.app.ui.MainActivityViewModel
import com.timewise.app.ui.agenda.AgendaScreen
import com.timewise.app.ui.navigation.Destination
import com.timewise.app.ui.navigation.InterstitialTriggerViewModel
import com.timewise.app.ui.onboarding.OnboardingScreen
import com.timewise.app.ui.premium.PremiumScreen
import com.timewise.app.ui.settings.SettingsScreen
import com.timewise.app.ui.statistics.StatisticsScreen
import com.timewise.app.ui.taskform.TaskFormScreen
import com.timewise.app.ui.theme.TimeWiseTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }
        enableEdgeToEdge()
        setContent {
            val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
            LaunchedEffect(startDestination) {
                if (startDestination != null) keepSplashOnScreen = false
            }
            TimeWiseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    startDestination?.let { destination ->
                        val navController = rememberNavController()

                        val activity = LocalActivity.current
                        val interstitialViewModel: InterstitialTriggerViewModel = hiltViewModel()

                        LaunchedEffect(navController) {
                            navController.currentBackStackEntryFlow.collect {
                                if (activity != null) {
                                    interstitialViewModel.maybeShowAd(activity)
                                }
                            }
                        }

                        val bottomBarRoutes = setOf(
                            Destination.Agenda.route,
                            Destination.Calendar.route,
                            Destination.Statistics.route,
                            Destination.Settings.route
                        )
                        val backStackEntry by navController.currentBackStackEntryAsState()
                        val showBottomBar = backStackEntry?.destination?.route in bottomBarRoutes

                        Scaffold(
                            bottomBar = {
                                if (showBottomBar) {
                                    AppBottomBar(navController = navController)
                                }
                            }
                        ) { padding ->
                            NavHost(
                                navController = navController,
                                startDestination = destination,
                                modifier = Modifier.padding(padding)
                            ) {
                                composable(Destination.Onboarding.route) {
                                    OnboardingScreen(
                                        onFinished = {
                                            navController.navigate(Destination.Agenda.route) {
                                                popUpTo(Destination.Onboarding.route) { inclusive = true }
                                            }
                                        }
                                    )
                                }
                                composable(Destination.Agenda.route) {
                                    AgendaScreen(
                                        onAddTaskClick = {
                                            navController.navigate(Destination.TaskFormCreate.route)
                                        },
                                        onTaskClick = { task ->
                                            navController.navigate(
                                                Destination.TaskFormEdit.createRoute(task.id)
                                            )
                                        }
                                    )
                                }
                                composable(Destination.Calendar.route) {
                                    // TODO: sustituir por TimeBlockingScreen real cuando esté conectada (Card #17).
                                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                        Text(stringResource(R.string.premium_coming_soon))
                                    }
                                }
                                composable(Destination.Statistics.route) {
                                    StatisticsScreen(
                                        onUpgradeClick = { navController.navigate(Destination.Premium.route) }
                                    )
                                }
                                composable(Destination.Settings.route) {
                                    SettingsScreen(
                                        onNavigateBack = { navController.popBackStack() },
                                        onNavigateToPremium = { navController.navigate(Destination.Premium.route) }
                                    )
                                }
                                composable(Destination.Premium.route) {
                                    PremiumScreen(onNavigateBack = { navController.popBackStack() })
                                }
                                composable(Destination.TaskFormCreate.route) {
                                    TaskFormScreen(
                                        onTaskSaved = { navController.popBackStack() },
                                        onNavigateBack = { navController.popBackStack() }
                                    )
                                }
                                composable(
                                    route = Destination.TaskFormEdit.route,
                                    arguments = listOf(navArgument("taskId") { type = NavType.LongType })
                                ) {
                                    TaskFormScreen(
                                        onTaskSaved = { navController.popBackStack() },
                                        onNavigateBack = { navController.popBackStack() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class TabItem(val route: String, val label: String, val icon: ImageVector)

@Composable
private fun AppBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val tabs = listOf(
        TabItem(Destination.Agenda.route, stringResource(R.string.nav_home), Icons.Filled.Home),
        TabItem(Destination.Calendar.route, stringResource(R.string.nav_agenda), Icons.Filled.CalendarMonth),
        TabItem(Destination.Statistics.route, stringResource(R.string.nav_statistics), Icons.Filled.BarChart),
        TabItem(Destination.Settings.route, stringResource(R.string.nav_settings), Icons.Filled.Settings)
    )

    NavigationBar {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                }
            )
        }
    }
}