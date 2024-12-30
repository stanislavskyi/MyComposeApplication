package com.hfad.mycomposeapplication.ui.screens.account

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.AppTheme
import com.hfad.mycomposeapplication.MyAppNavigation
import com.hfad.mycomposeapplication.domain.entity.Friend

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: FriendsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState() // Получаем состояние

    when(state) {
        is AccountState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center) ,
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }

        is AccountState.Content ->  {
            val friends = (state as AccountState.Content).list

            if (friends.isEmpty()){
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    text = "Вы единственный пользователь"
                )
            } else{
                LazyColumn(modifier = modifier) {
                    items(items = friends) { friend ->
                        FriendItem(
                            friend = friend,
                            lambdaClickButton = { viewModel.changeSubState(friend) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FriendItem(
    modifier: Modifier = Modifier,
    friend: Friend,
    lambdaClickButton: () -> Unit
) {
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
            Text(text = if (friend.subscription) "Добавить" else "Удалить")
        }
    }
}

@Preview
@Composable
fun AccountScreenPreview() {
    AppTheme {
        AccountScreen()
    }
//    MyComposeApplicationTheme {
//        MyAppNavigation()
//    }
}