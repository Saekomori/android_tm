package com.example.tm.graphs

import androidx.annotation.StringRes
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tm.R
import com.example.tm.screens.auth.AuthScreen
import com.example.tm.screens.auth.RegScreen


fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = TaskManagerScreen.Start.name
    ) {
        composable(route = TaskManagerScreen.Start.name) {
            AuthScreen(
                regGoClick = {navController.navigate(TaskManagerScreen.Reg.name)},
                loginClick = {navController.navigate(Graph.MAIN)},
            )
        }

        composable(route = TaskManagerScreen.Reg.name) {
            RegScreen(
                authGo = {navController.navigate(TaskManagerScreen.Start.name)},
                tasksGo = {navController.navigate(Graph.MAIN)},
            )
        }


    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object Forgot : AuthScreen(route = "FORGOT")
}

enum class TaskManagerScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Reg(title = R.string.reg_name),
    Main(title = R.string.main_name),
}