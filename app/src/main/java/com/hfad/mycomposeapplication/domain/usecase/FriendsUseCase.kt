package com.hfad.mycomposeapplication.domain.usecase

import com.hfad.mycomposeapplication.domain.entity.Friend
import com.hfad.mycomposeapplication.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FriendsUseCase @Inject constructor(private val accountRepository: AccountRepository) {
//    suspend operator fun invoke(): Flow<List<Friend>> {
//        return accountRepository.get()
//    }
}

