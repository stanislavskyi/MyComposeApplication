package com.hfad.mycomposeapplication

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Register : Routes("register")
    object Account : Routes("account")
}