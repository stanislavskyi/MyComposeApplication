package com.hfad.mycomposeapplication

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun MyNavigationBar(navController: NavHostController) {
    NavigationBar {
        Routes.destinations.forEach { screen ->
            val currentDestination by navController.currentBackStackEntryAsState()
            val selected = currentDestination?.destination?.route == screen.route


            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(imageVector =  screen.icon, contentDescription = stringResource(screen.labelRes)) },
                //label = { Text(stringResource(screen.labelRes)) }
            )
        }
    }
}