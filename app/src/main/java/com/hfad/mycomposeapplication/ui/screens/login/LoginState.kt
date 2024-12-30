package com.hfad.mycomposeapplication.ui.screens.login

sealed class LoginState {
    object Idle : LoginState()
    object Success : LoginState()
    sealed class Error(val message: String) : LoginState() {
        object InvalidEmail : Error("Invalid email format")
        object InvalidPassword : Error("Password is too short")
        object AuthenticationFailed : Error("Incorrect credentials")
    }
}
