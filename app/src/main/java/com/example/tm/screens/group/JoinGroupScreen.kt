package com.example.tm.screens.group

import TokenManager
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tm.viewModels.GroupViewModel

@Composable
fun JoinGroupScreen(
    inviteToken: String,
    navController: NavController,
    viewModel: GroupViewModel = viewModel()
){
    val token = TokenManager.token

    if(token == null){
        navController.navigate("auth_graph")
    } else {
        viewModel.joinGroup(inviteToken)
        navController.navigate("main_graph")
    }
}