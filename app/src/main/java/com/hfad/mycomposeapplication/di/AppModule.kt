package com.hfad.mycomposeapplication.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.mycomposeapplication.data.network.DeezerApiService
import com.hfad.mycomposeapplication.data.network.RetrofitInstance
import com.hfad.mycomposeapplication.data.repository.AccountRepositoryImpl
import com.hfad.mycomposeapplication.data.repository.AuthRepositoryImpl
import com.hfad.mycomposeapplication.domain.repository.AccountRepository
import com.hfad.mycomposeapplication.domain.repository.AuthRepository
import com.hfad.mycomposeapplication.domain.usecase.FriendsUseCase
import com.hfad.mycomposeapplication.domain.usecase.LoginUseCase
import com.hfad.mycomposeapplication.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideAccountRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AccountRepository {
        return AccountRepositoryImpl(auth = auth,firestore = firestore)
    }

    @Provides
    fun provideFriendsUseCase(repository: AccountRepository): FriendsUseCase {
        return FriendsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeezerApiService(): DeezerApiService {
        return RetrofitInstance.api
    }

    @Provides 
    @ApplicationContext
    fun provideApplicationContext(app: Application): Application = app

}