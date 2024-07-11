package com.example.tm.graphs

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.tm.screens.group.JoinGroupScreen
import com.example.tm.screens.MainScreen
import com.example.tm.screens.group.GroupDetailScreen
import com.example.tm.screens.group.TaskGroupDetailScreen
import com.example.tm.screens.task.TaskDetailScreen
import com.example.tm.ui.theme.AppTheme

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTHENTICATION
    ) {

        authNavGraph(navController = navController)

        composable(route = Graph.MAIN) {
            AppTheme {


             MainScreen(
                 createTask = {navController.navigate(Graph.TASK)},

                 navController = navController
             )
            }
        }

        composable(
            route = "taskDetail_screen/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            AppTheme {


            val taskId = backStackEntry.arguments?.getString("taskId") ?: throw IllegalArgumentException("Task ID is required")
            TaskDetailScreen(taskId = taskId, navController = navController)
            }
        }

        composable(
            route = "groupDetail_screen/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            AppTheme {


            val groupId = backStackEntry.arguments?.getString("groupId") ?: throw IllegalArgumentException("Group ID is required")
            GroupDetailScreen(groupId = groupId, navController = navController)
            }
        }
        composable(
            route = "taskGroupDetail_screen/{taskGroupId}",
            arguments = listOf(navArgument("taskGroupId") { type = NavType.StringType })
        ) { backStackEntry ->
            AppTheme {


            val taskGroupId = backStackEntry.arguments?.getString("taskGroupId") ?: throw IllegalArgumentException("taskGroup ID is required")
            TaskGroupDetailScreen(taskGroupId = taskGroupId, navController = navController)
            }
        }

//        composable(
//            route = "join/{inviteToken}",
//            arguments = listOf(navArgument("inviteToken") { type = NavType.StringType })
//        )
//        { backStackEntry ->
//            val inviteToken = backStackEntry.arguments?.getString("inviteToken") ?: throw IllegalArgumentException("Invite token is required")
//            JoinGroupScreen(inviteToken = inviteToken, navController = navController)
//        }

        composable(
            route = "join",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://tmsharedgroup.com/join/{inviteToken}"
                    action = Intent.ACTION_VIEW
                }
            ),
            arguments = listOf(
                navArgument("inviteToken") {
                    type = NavType.StringType
                }
            )
        )
        {   backStackEntry ->
            val inviteToken = backStackEntry.arguments?.getString("inviteToken") ?: throw IllegalArgumentException("Invite token is required")
            JoinGroupScreen(inviteToken = inviteToken, navController = navController)
        }

    }
}

object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
    const val DETAILS = "detailsTask_graph"
    const val TASK = "task_graph"
}