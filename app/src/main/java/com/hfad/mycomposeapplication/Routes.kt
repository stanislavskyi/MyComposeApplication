package com.hfad.mycomposeapplication

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Routes(val route: String, @StringRes val labelRes: Int, val icon: ImageVector ) {
    object Login : Routes(
        route = "login",
        labelRes = R.string.app_name,
        icon =Icons.Default.Email
    )

    object Register : Routes(
        route ="register",
        labelRes =R.string.app_name,
        icon = Icons.Default.Email
    )
    object Account : Routes(
        route ="account",
        labelRes = R.string.app_name,
        icon = Icons.Default.AccountCircle
    )

    object TopChart: Routes(
        route = "topchart",
        labelRes = R.string.app_name,
        icon = Icons.Default.Star
    )

    object Caster: Routes(
        route = "caster",
        labelRes = R.string.app_name,
        icon = Icons.Default.Call
    )

    companion object {
        val destinations = listOf(Account, TopChart)
    }
}




