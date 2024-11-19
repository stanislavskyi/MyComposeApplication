package com.hfad.mycomposeapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hfad.mycomposeapplication.ui.screens.account.AccountScreen
import com.hfad.mycomposeapplication.ui.screens.LoginScreen
import com.hfad.mycomposeapplication.ui.screens.RegisterScreen
import com.hfad.mycomposeapplication.ui.common.navigation.ScreenRoutes
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
fun MyApp() {
    MyComposeApplicationTheme {

        var screenRoutes by remember { mutableStateOf<ScreenRoutes>(ScreenRoutes.Register) }

        Scaffold(
            bottomBar = {}
        ) { padding ->
            when(screenRoutes){
                ScreenRoutes.Account -> {
                    AccountScreen(
                        Modifier.padding(padding)
                    )
                }
                ScreenRoutes.Login -> {
                    LoginScreen(
                        Modifier.padding(padding),
                        lambdaClickButton = { screenRoutes = ScreenRoutes.Register }
                    )
                }
                ScreenRoutes.Register -> {
                    RegisterScreen(
                        Modifier.padding(padding),
                        lambdaClickButton = { screenRoutes = ScreenRoutes.Login },
                        lambdaNavigationClick = { screenRoutes = ScreenRoutes.Account }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MyComposeApplicationTheme {
        MyApp()
    }
}