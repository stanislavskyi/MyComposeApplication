package com.hfad.mycomposeapplication

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hfad.mycomposeapplication.ui.common.components.CasterBar
import com.hfad.mycomposeapplication.ui.screens.account.AccountScreen
import com.hfad.mycomposeapplication.ui.screens.caster.CasterScreen
import com.hfad.mycomposeapplication.ui.screens.library.LibraryScreen
import com.hfad.mycomposeapplication.ui.screens.login.LoginScreen
import com.hfad.mycomposeapplication.ui.screens.register.RegisterScreen
import com.hfad.mycomposeapplication.ui.screens.topchart.ChartScreen
import com.hfad.mycomposeapplication.ui.screens.topchart.TopChartViewModel
import kotlinx.coroutines.launch


@Composable
fun LanguageSelectionDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf("English", "Русский") // Пример списка языков

    if (openDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Select Language") },
            text = {
                LazyColumn {
                    items(languages) { language ->
                        Text(
                            text = language,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onLanguageSelected(language)
                                    onDismiss()
                                }
                                .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun MyAppNavigation(
    navController: NavHostController = rememberNavController(),
    sharedViewModel: TopChartViewModel = hiltViewModel()
) {

    val isPlaying by sharedViewModel.isPlaying.collectAsStateWithLifecycle()

    var toCasterScreen by rememberSaveable { mutableStateOf(false) }

    var openDialog by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") } // Состояние для вы

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Current language: $selectedLanguage", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                        ) {
                            // Иконка
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "Home",
                                modifier = Modifier.padding(end = 8.dp) // Отступ между иконкой и текстом
                            )
                            // Текст
                            Text(text = "Language")
                        }
                    },
                    selected = false,
                    onClick = {
                        openDialog = true
                    }
                )
            }
        }
    ) {

        Scaffold(
            floatingActionButton = {
                val dest = navController.currentBackStackEntryAsState().value?.destination?.route
                when (dest) {
                    Routes.Library.route -> {
//                    FloatingActionButton(
//                        onClick = {  },
//                        containerColor = MaterialTheme.colorScheme.primary,
//                        contentColor = MaterialTheme.colorScheme.onPrimary
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Add,
//                            contentDescription = stringResource(id = R.string.email)
//                        )
//                    }
                    }

                    else -> {}
                }
            },
            topBar = {
                TopBar(
                    navController = navController,
                    lambda = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            bottomBar = {
                BottomBar(navController = navController)
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination = Routes.TopChart.route,
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None }
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
                    composable(
                        Routes.TopChart.route,
                    ) {
                        ChartScreen(
                            viewModel = sharedViewModel
                        )
                    }
                    composable(Routes.Library.route) {
                        LibraryScreen(
                            viewModel = sharedViewModel
                        )
                    }
                }
                if (isPlaying.isPlaying) {
                    CasterBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter), // Учитываем высоту NavigationBar
                        viewModel = sharedViewModel,
                        lambdaToCaster = {
                            toCasterScreen = true
                            //navController.navigateSingleTopTo(Routes.Caster.route)
                        }
                    )
                }

            }
        }
    }

    LanguageSelectionDialog(
        openDialog = openDialog,
        onDismiss = { openDialog = false },
        onLanguageSelected = { language ->
            selectedLanguage = language
        }
    )
    // Диалоговое окно
//    if (openDialog) {
//        val languages = listOf("English", "Русский")
//        AlertDialog(
//            onDismissRequest = { openDialog = false },
//            title = { Text("Language Settings") },
//            text = {
//                Column {
//                    languages.forEach { language ->
//                        Text(
//                            text = language,
//                            modifier = Modifier.padding(8.dp)
//                                .clickable { openDialog = false }
//                        )
//                    }
//                }
//            },
//            confirmButton = {
//                TextButton(onClick = { openDialog = false }) {
//                    Text("OK")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { openDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }

    AnimatedVisibility(
        visible = toCasterScreen,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 200, easing = EaseIn)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 200, easing = EaseOut)
        )
    ) {
        CasterScreen(
            viewModel = sharedViewModel,
            backLambda = { toCasterScreen = false }
        )
        BackHandler {
            toCasterScreen = false
        }
    }

}

@Composable
fun BottomBar(navController: NavHostController){
    val dest = navController.currentBackStackEntryAsState().value?.destination?.route
    if (dest != Routes.Login.route && dest != Routes.Register.route) {
        NavigationBar {
            Routes.destinations.forEach { screen ->
                val currentDestination by navController.currentBackStackEntryAsState()
                val selected = currentDestination?.destination?.route == screen.route

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route) {
//                                    popUpTo(navController.graph.startDestinationId) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
                            //переходим в топ икон, нажимаем на чарт-экран, и кнопка никак не реагирует
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = stringResource(screen.labelRes)
                        )
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, lambda: () -> Unit) {
    val dest = navController.currentBackStackEntryAsState().value?.destination?.route
    if (dest != Routes.Login.route && dest != Routes.Register.route) {
        TopAppBar(

            title = {
                Text(
                    text = "account",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            actions = {
                IconButton(onClick = {
                    navController.navigateSingleTopTo(Routes.Account.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = stringResource(id = R.string.app_name),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }


            },
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                FilledIconButton(
                    onClick = {
                        lambda()

                    },
                    modifier = Modifier.padding(8.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        )
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }