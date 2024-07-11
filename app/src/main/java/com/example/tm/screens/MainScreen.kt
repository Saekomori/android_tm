
package com.example.tm.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tm.bottom_navigation.BottomNavigationBar
import com.example.tm.graphs.NavGraph
import com.example.tm.ui.theme.AppTheme
import com.example.tm.ui.theme.components.MainAppBar


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    createTask: () -> Unit,
    navController: NavController
) {
    val navControllerBott = rememberNavController()
    val topBarTitle = remember { mutableStateOf("") }
    val currentBackStackEntry by navControllerBott.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        when (currentBackStackEntry?.destination?.route) {
            "task_screen" -> topBarTitle.value = "Задачи"
            "groups_screen" -> topBarTitle.value = "Группы"
            "setting_screen" -> topBarTitle.value = "Настройки"
            else -> topBarTitle.value = "Menu"
        }
    }

    AppTheme {
        Scaffold(
            modifier = Modifier.safeDrawingPadding(),
            bottomBar = { BottomNavigationBar(navController = navControllerBott) }
        ) { innerPadding ->
            NavGraph(
                navHostController = navControllerBott,
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                createTask = createTask
            )
        }
    }
}