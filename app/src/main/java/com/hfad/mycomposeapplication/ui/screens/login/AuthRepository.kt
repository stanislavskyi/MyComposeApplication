package com.hfad.mycomposeapplication.ui.screens.login

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

object AuthRepositoryImpl {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun register(email: String, password: String): Result<Boolean> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}