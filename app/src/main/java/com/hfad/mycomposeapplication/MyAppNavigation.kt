package com.hfad.mycomposeapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hfad.mycomposeapplication.ui.screens.register.RegisterScreen
import com.hfad.mycomposeapplication.ui.screens.account.AccountScreen
import com.hfad.mycomposeapplication.ui.screens.login.LoginScreen

@Composable
fun MyAppNavigation(
    navController: NavHostController = rememberNavController()
) {
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Login.route) {
                LoginScreen(
                    lambdaClickButton = { navController.navigateSingleTopTo(Routes.Register.route) },
                    onNavigateToNextScreen = { navController.navigateSingleTopTo(Routes.Account.route) }
                )
            }
            composable(Routes.Register.route) {
                RegisterScreen(
                    lambdaClickButton = { navController.navigateSingleTopTo(Routes.Login.route) },
                    onNavigateToNextScreen = { navController.navigateSingleTopTo(Routes.Account.route) }
                )
            }
            composable(Routes.Account.route) {
                AccountScreen()
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }
