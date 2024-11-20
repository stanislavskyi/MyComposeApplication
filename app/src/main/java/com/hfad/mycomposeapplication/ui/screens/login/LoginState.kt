package com.hfad.mycomposeapplication.ui.screens.login

sealed class LoginState {
    object Idle : LoginState() // Начальное состояние
    object Success : LoginState() // Успешная валидация
    data class Error(val message: String) : LoginState() // Ошибка валидации
}
