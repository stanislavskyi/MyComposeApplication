package com.hfad.mycomposeapplication.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    private val authRepositoryImpl = AuthRepositoryImpl

    fun validateEmailAndPassword(email: String, password: String){
        if (email.isNotEmpty() && password.isNotEmpty()){
            Log.d("MY_TAG", "true")
            _loginState.value = LoginState.Success
        } else  {
            Log.d("MY_TAG", "false")
            _loginState.value = LoginState.Error("error validate")
        }
    }



    fun resetLoginState(){
        _loginState.value = LoginState.Idle
    }


}

