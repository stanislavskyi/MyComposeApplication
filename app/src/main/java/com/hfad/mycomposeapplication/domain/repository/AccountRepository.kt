package com.hfad.mycomposeapplication.domain.repository


import com.hfad.mycomposeapplication.domain.entity.Friend
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun addFriend(friend: Friend)
    suspend fun deleteFriend(friend: Friend)
    fun listenForUserChanges(): Flow<List<Friend>>
}