package com.example.tm.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tm.screens.group.GroupsScreen
import com.example.tm.screens.SettingScreen
import com.example.tm.screens.task.TaskScreen
import com.example.tm.ui.theme.AppTheme

@Composable
fun NavGraph(
    navHostController: NavHostController,
    navController: NavController,
    modifier: Modifier,
    createTask: ()  -> Unit
) {
    NavHost(navController = navHostController, startDestination = "task_screen", modifier = modifier ) {
        composable("task_screen") {
            AppTheme {


            TaskScreen (
                modifier = Modifier,
                navController = navController
            )
            }
        }
        composable("groups_screen") {
            AppTheme {


            GroupsScreen(
                navController = navController
            )
            }
        }
        composable("setting_screen") {
            AppTheme {


            SettingScreen(
                navController = navController
            )
            }
        }
    }
}
