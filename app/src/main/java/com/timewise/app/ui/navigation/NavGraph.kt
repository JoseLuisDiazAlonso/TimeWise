package com.timewise.app.ui.navigation


import androidx.compose.runtime.Composable

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.timewise.app.ui.agenda.AgendaScreen
import com.timewise.app.ui.taskform.TaskFormScreen

/**
 * Registramos la pantalla al Grafo y aceptando un id y conectando los puntos de entrada
 * y salida de la agenda.
 * **/
@Composable
fun AppNavGraph () {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "agenda"
    ) {
        composable("agenda") {
            AgendaScreen(
                onTaskClick = { task ->
                    navController.navigate("taskForm?taskId=${task.id}")
                },
                onAddTaskClick = {
                    navController.navigate("taskForm")
                }
            )
        }
        composable(
            route = "taskForm?taskId = {taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            TaskFormScreen(
                onTaskSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}



