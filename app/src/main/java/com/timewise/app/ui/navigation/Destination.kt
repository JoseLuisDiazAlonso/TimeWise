package com.timewise.app.ui.navigation

sealed class Destination(val route: String) {
    object Onboarding : Destination("onboarding")
    object Agenda : Destination("agenda")
    object Calendar : Destination("calendar")
    object Statistics : Destination("statistics")
    object TaskFormCreate : Destination("task_form")
    object TaskFormEdit : Destination("task_form/{taskId}") {
        fun createRoute(taskId: Long) = "task_form/$taskId"
    }
    object Settings : Destination("settings")
    object Premium : Destination("premium")
    object Paywall : Destination("paywall")
    object Export : Destination("export")
}