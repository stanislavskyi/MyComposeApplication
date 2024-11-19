package com.hfad.mycomposeapplication.ui.screens.account

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hfad.mycomposeapplication.ui.theme.MyComposeApplicationTheme

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    friendsViewModel: FriendsViewModel = viewModel()
) {
    val friends by friendsViewModel.state.collectAsState()
    FriendList(
        friends = friends,
        viewModel = friendsViewModel

    )
}


@Composable
fun FriendList(
    modifier: Modifier = Modifier,
    friends: List<Friend>,
    viewModel: FriendsViewModel
){
    LazyColumn(modifier = modifier) {
        items(items = friends){ friend ->
            FriendItem(
                friend = friend,
                lambdaClickButton = { viewModel.changeSubState(friend) }
            )
        }
    }
}

@Composable
fun FriendItem(
    modifier: Modifier = Modifier,
    friend: Friend,
    lambdaClickButton: () -> Unit
){
    Row(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = friend.name
        )
        Button(
            onClick = { lambdaClickButton() }

        ) {
            Text(text = if(friend.subscription) "Добавить" else "Удалить")
        }
    }
}



@Preview
@Composable
fun AccountScreenPreview(){
    MyComposeApplicationTheme {
        AccountScreen()
    }
}