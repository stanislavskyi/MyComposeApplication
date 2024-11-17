package com.hfad.mycomposeapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hfad.mycomposeapplication.ui.theme.MyComposeApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}


@Composable
fun EditText(
    modifier: Modifier = Modifier,
    @StringRes text: Int
){
    OutlinedTextField(
        value = "",
        onValueChange = { },
//        leadingIcon = {
//            Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
//        },
        label = { Text(stringResource(text)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(56.dp)
    )
}

@Composable
fun LoginScreen(modifier: Modifier = Modifier){
    Column(modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        EditText(
            Modifier.padding(horizontal = 16.dp),
            text = R.string.password
        )
        EditText(
            Modifier.padding(horizontal = 16.dp),
            text = R.string.password
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { },
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.login_here))
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.register), style = MaterialTheme.typography.bodyMedium)
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
        }
    }
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    lambdaClickButton: () -> Unit){
    Column(modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        EditText(
            Modifier.padding(horizontal = 16.dp),
            text = R.string.email
        )
        EditText(
            Modifier.padding(horizontal = 16.dp),
            text = R.string.password
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick =  lambdaClickButton ,
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()) {
            Text(text = stringResource(id = R.string.register))
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.login_here), style = MaterialTheme.typography.bodyMedium)
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
        }
    }
}



@Composable
fun MyApp(){
    MyComposeApplicationTheme {

        var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

        Scaffold(
            bottomBar = {}
        ) { padding ->

            if (shouldShowOnboarding){
                RegisterScreen(
                    Modifier.padding(padding),
                    lambdaClickButton = { shouldShowOnboarding = false }
                )
            } else {
                LoginScreen(Modifier.padding(padding))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyComposeApplicationTheme {
        MyApp()
    }
}

object NavigationState {
    const val REGISTER_SCREEN = "Register"
    const val LOGIN_SCREEN = "Login"
}