package com.timewise.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.timewise.app.data.local.locale.LocaleHelper
import com.timewise.app.ui.MainActivityViewModel
import com.timewise.app.ui.agenda.AgendaScreen
import com.timewise.app.ui.navigation.Destination
import com.timewise.app.ui.navigation.InterstitialTriggerViewModel
import com.timewise.app.ui.onboarding.OnboardingScreen
import com.timewise.app.ui.taskform.TaskFormScreen
import com.timewise.app.ui.theme.TimeWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    // Sistema de idioma propio: envuelve el Context con el idioma guardado en
    // SharedPreferences, sin depender de AppCompatDelegate/LocaleManager (que no
    // funciona de forma fiable en esta ROM).
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

                        NavHost(
                            navController = navController,
                            startDestination = destination
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