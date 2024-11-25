package com.hfad.mycomposeapplication.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.mycomposeapplication.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun login(email: String, password: String) {
        if (!isValidEmail(email)) {
            _loginState.value = LoginState.Error("Invalid email format")
            return
        }
        if (!isValidPassword(password)) {
            _loginState.value = LoginState.Error("Password short")
            return
        }
        viewModelScope.launch {
            val result = loginUseCase(email, password)
            if (result.isSuccess) {
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error("The supplid autn sredential is incorrect")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean = email.contains("@")
    private fun isValidPassword(password: String): Boolean = password.length >= VALID_PASSWORD

    companion object {
        private const val VALID_PASSWORD = 6
    }

}



