package com.hfad.mycomposeapplication.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.mycomposeapplication.data.repository.AccountRepositoryImpl
import com.hfad.mycomposeapplication.domain.repository.AccountRepository
import com.hfad.mycomposeapplication.domain.usecase.FriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val friendsUseCase: FriendsUseCase,
    private val repositoryImpl: AccountRepositoryImpl
): ViewModel() {

    private val _friendsState = MutableStateFlow<List<Friend>>(emptyList())
    val friendsState = _friendsState.asStateFlow()

    init {
        viewModelScope.launch {
            repositoryImpl.listenForUserChanges().collect{
                _friendsState.value = it.map { Friend(it.name, it.subscription) }
            }
        }
    }

    fun changeSubState(friend: Friend) {
        if (friend.subscription){
            addFriend(friend)
        } else { deleteFriend(friend) }

        val updateList = _friendsState.value.map {
            if (it.name == friend.name){
                it.copy(subscription = !it.subscription)
            }else it
        }
        _friendsState.value = updateList
    }

    private fun addFriend(friend: Friend){
        viewModelScope.launch {
            repositoryImpl.addFriend(
                friend = com.hfad.mycomposeapplication.domain.entity.Friend(friend.name, friend.subscription)
            )
        }
    }

    private fun deleteFriend(friend: Friend){
        viewModelScope.launch {
            repositoryImpl.deleteFriend(
                friend = com.hfad.mycomposeapplication.domain.entity.Friend(friend.name, friend.subscription)
            )
        }
    }
}