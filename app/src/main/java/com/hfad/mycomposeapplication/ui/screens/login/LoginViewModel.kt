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
        when {
            email.isValidEmail().not() -> _loginState.value = LoginState.Error.InvalidEmail
            password.isValidPassword().not() -> _loginState.value = LoginState.Error.InvalidPassword
            else -> {
                viewModelScope.launch {
                    val result = loginUseCase(email, password)
                    if (result.isSuccess) {
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value = LoginState.Error.AuthenticationFailed
                    }
                }
            }
        }
    }

    private fun String.isValidEmail() = contains("@") && length > 5
    private fun String.isValidPassword() = length >= VALID_PASSWORD

    companion object {
        private const val VALID_PASSWORD = 6
    }
}



