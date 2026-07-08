package com.timewise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.timewise.app.ui.agenda.AgendaScreen
import com.timewise.app.ui.theme.TimeWiseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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