package com.hfad.mycomposeapplication.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.hfad.mycomposeapplication.domain.repository.AccountRepository
import com.hfad.mycomposeapplication.domain.usecase.RegisterUseCase
import com.hfad.mycomposeapplication.ui.screens.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): ViewModel() {

    private val _registerState = MutableStateFlow<LoginState>(LoginState.Idle)
    val registerState = _registerState.asStateFlow()

    fun resetLoginState() {
        _registerState.value = LoginState.Idle
    }

    fun register(email: String, password: String) {
        if (!isValidEmail(email)) {
            _registerState.value = LoginState.Error.InvalidEmail
            return
        }
        if (!isValidPassword(password)) {
            _registerState.value = LoginState.Error.InvalidPassword
            return
        }
        viewModelScope.launch {
            val result = registerUseCase(email, password)
            if (result.isSuccess) {
                _registerState.value = LoginState.Success
            } else {
                _registerState.value = LoginState.Error.AuthenticationFailed
            }
        }
    }

    fun addCurrentUser(){
        viewModelScope.launch {
            val userId = auth.currentUser?.uid
            val email = auth.currentUser?.email ?: ""
            val name = email.substringBefore("@")
            val user = hashMapOf(
                "isPremium" to true,
                "name" to  name
            )

            firestore.collection("users").document(userId!!).set(
                user,
                SetOptions.merge()
            ).await()
        }
    }

    private fun isValidEmail(email: String): Boolean = email.contains("@")
    private fun isValidPassword(password: String): Boolean = password.length >= VALID_PASSWORD

    companion object {
        private const val VALID_PASSWORD = 6
    }
}