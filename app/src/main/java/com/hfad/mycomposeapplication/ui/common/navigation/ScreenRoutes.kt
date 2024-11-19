package com.hfad.mycomposeapplication.ui.common.navigation

sealed class ScreenRoutes{
    object Register: ScreenRoutes()
    object Login: ScreenRoutes()
    object Account: ScreenRoutes()
}
