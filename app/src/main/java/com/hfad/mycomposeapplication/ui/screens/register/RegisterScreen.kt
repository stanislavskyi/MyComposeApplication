package com.hfad.mycomposeapplication.ui.screens.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hfad.mycomposeapplication.R
import com.hfad.mycomposeapplication.ui.common.components.EditText
import com.hfad.mycomposeapplication.ui.screens.login.LoginViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    lambdaClickButton: () -> Unit,
    lambdaNavigationClick: () -> Unit,
    loginViewModel: RegisterViewModel = hiltViewModel()
) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        EditText(
            Modifier.padding(horizontal = 16.dp),
            labelStringRes = R.string.email,
            keyboardType = KeyboardType.Email,
            onValueChange = { emailText = it},
            value = emailText
        )
        EditText(
            Modifier.padding(horizontal = 16.dp),
            labelStringRes = R.string.password,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { passwordText = it},
            value = passwordText
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                loginViewModel.register(email = emailText, password = passwordText, onSuccess = {}, onError = {})
            },
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.register))
        }
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                lambdaClickButton()
            }
        ) {
            Text(
                text = stringResource(R.string.login_here),
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
        }
    }
}