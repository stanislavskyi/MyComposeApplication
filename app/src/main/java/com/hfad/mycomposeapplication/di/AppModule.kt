package com.hfad.mycomposeapplication.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.mycomposeapplication.data.database.AppDatabase
import com.hfad.mycomposeapplication.domain.repository.DatabaseRepository
import com.hfad.mycomposeapplication.data.repository.DatabaseRepositoryImpl
import com.hfad.mycomposeapplication.data.database.MusicDao
import com.hfad.mycomposeapplication.data.mapper.MusicMapper
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
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    fun provideAccountRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AccountRepository {
        return AccountRepositoryImpl(auth = auth, firestore = firestore)
    }

    @Provides
    fun provideDatabaseRepository(musicDao: MusicDao, musicMapper: MusicMapper): DatabaseRepository {
        return DatabaseRepositoryImpl(musicDao, musicMapper)
    }

    // UseCases

    @Provides
    fun provideRegisterUseCase(repository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(repository)
    }

    @Provides
    fun provideLoginUseCase(repository: AuthRepository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideFriendsUseCase(repository: AccountRepository): FriendsUseCase {
        return FriendsUseCase(repository)
    }

    // Firebase and Deezer service

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()


    @Provides
    @Singleton
    fun provideDeezerApiService(): DeezerApiService = RetrofitInstance.api

    // Database

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration().build()
        }

    @Provides 
    @ApplicationContext
    fun provideApplicationContext(app: Application): Application = app

    @Provides
    fun provideDao(database: AppDatabase): MusicDao = database.musicDao()



}