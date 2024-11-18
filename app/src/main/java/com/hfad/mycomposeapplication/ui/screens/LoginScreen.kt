package com.hfad.mycomposeapplication.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.hfad.mycomposeapplication.R
import com.hfad.mycomposeapplication.ui.common.components.EditText

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    lambdaClickButton: () -> Unit) {
    Column(
        modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        EditText(
            Modifier.padding(horizontal = 16.dp),
            labelStringRes = R.string.email,
            keyboardType = KeyboardType.Email
        )
        EditText(
            Modifier.padding(horizontal = 16.dp),
            labelStringRes = R.string.password,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { },
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.login_here))
        }
        Spacer(Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                lambdaClickButton()
            }
        ) {
            Text(
                text = stringResource(R.string.register),
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
        }
    }
}