package com.hfad.mycomposeapplication.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hfad.mycomposeapplication.data.repository.AccountRepositoryImpl
import com.hfad.mycomposeapplication.domain.entity.Friend
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AccountState{
    object Loading: AccountState()
    data class Content(val list: List<Friend>): AccountState()
}

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val repositoryImpl: AccountRepositoryImpl
): ViewModel() {

//    private val _friendsState = MutableStateFlow<List<Friend>>(emptyList())
//    val friendsState = _friendsState.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(true)
//    val isLoading = _isLoading.asStateFlow()

    private val _state = MutableStateFlow<AccountState>(AccountState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repositoryImpl.listenForUserChanges().collect{
//                _friendsState.value = it.map { Friend(it.name, it.subscription) }
//                _isLoading.value = false
                val listFriend = AccountState.Content(it.map { Friend(it.name, it.subscription) })
                _state.value = listFriend
            }
        }
    }

    fun changeSubState(friend: Friend) {
        val updatedFriend = friend.copy(subscription = !friend.subscription)

//        val updateList = _friendsState.value.map {
//            if (it.name == friend.name) updatedFriend else it
//        }
//        _friendsState.value = updateList

        val updatedList = (_state.value as? AccountState.Content)?.list?.map {
            if (it.name == friend.name) updatedFriend else it
        } ?: return

        _state.value = AccountState.Content(updatedList)

//        val i = _state.value
//        if (i is AccountState.Content){
//            i.list.map {
//                if (it.name == friend.name) updatedFriend else it
//            }
//            _state.value = i
//        }


        if (friend.subscription){
            addFriend(friend)
        } else { deleteFriend(friend) }
    }

    private fun addFriend(friend: Friend){
        viewModelScope.launch {
            repositoryImpl.addFriend(
                friend = Friend(friend.name, friend.subscription)
            )
        }
    }

    private fun deleteFriend(friend: Friend){
        viewModelScope.launch {
            repositoryImpl.deleteFriend(
                friend = Friend(friend.name, friend.subscription)
            )
        }
    }
}