package com.hfad.mycomposeapplication.ui.screens.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FriendsViewModel : ViewModel() {

    private val friends: List<Friend> = mutableListOf(
        Friend("first account", true),
        Friend("second account", false),
        Friend("third account", true),
        Friend("five account", false),
        Friend("six account", false),
        Friend("seven account", true),
        Friend("ten account", true),
        Friend("eleven account", false),
        Friend("twenty account", true)
    )

    private val _state = MutableStateFlow(friends)
    val state = _state.asStateFlow()

    init {
        for (friend in friends) {
            _state.value = friends
        }
    }

    fun changeSubState(friend: Friend) {
        val updateList = _state.value.map {
            if (it.name == friend.name){
                it.copy(subscription = !it.subscription)
            }else it
        }
        _state.value = updateList
    }
}