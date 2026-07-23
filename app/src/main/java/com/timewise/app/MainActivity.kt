package com.timewise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.timewise.app.ui.MainActivityViewModel
import com.timewise.app.ui.agenda.AgendaScreen
import com.timewise.app.ui.theme.TimeWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

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
                    // Pantalla temporal: cuando llegues al card de navegación,
                    // esto se sustituirá por un NavHost con rutas.
                    AgendaScreen()
                }
            }
        }
    }
}