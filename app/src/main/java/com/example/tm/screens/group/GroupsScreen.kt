package com.example.tm.screens.group

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tm.R
import com.example.tm.data.SubTask
import com.example.tm.entity.Group
import com.example.tm.entity.Task
import com.example.tm.screens.task.TaskItem
import com.example.tm.ui.theme.AppTheme
import com.example.tm.ui.theme.components.ButtonFab
import com.example.tm.ui.theme.components.ButtonTask
import com.example.tm.ui.theme.components.CustomDateTimePicker
import com.example.tm.ui.theme.components.MyOutlinedTextField
import com.example.tm.ui.theme.components.SubTaskAdd
import com.example.tm.ui.theme.components.SubTaskText
import com.example.tm.ui.theme.components.TaskText
import com.example.tm.viewModels.GroupViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GroupsScreen(
    viewModel: GroupViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var nameGroup by rememberSaveable { mutableStateOf("") }
    AppTheme {


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Группы") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            )
        },
        floatingActionButton = {
            ButtonFab { showBottomSheet = true }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { padding ->

        LaunchedEffect(Unit) {
            viewModel.loadGroupsUser()
            viewModel.loadGroupsCreator()
        }

        val groupsCreator = viewModel.groupsCreator.collectAsState().value
        val groupsUser = viewModel.groupsUser.collectAsState().value

        LazyColumn(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(groupsCreator) { group ->
                GroupItem(
                    group = group,
                    onClick = { groupId ->
                        navController.navigate("groupDetail_screen/$groupId")
                    },
                    rights = "Создатель"
                )
            }
            items(groupsUser) { group ->
                GroupItem(
                    group = group,
                    onClick = { groupId ->
                        navController.navigate("groupDetail_screen/$groupId")
                    },
                    rights = "Участник"
                )
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                ) {
                    MyOutlinedTextField(
                    text = nameGroup,
                    placeholder = "Наименование группы",
                    label = "Наименование группы",
                    onTextChanged = { nameGroup = it },
                   keyboardType = KeyboardType.Email
                )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ButtonTask(text = "Отмена", onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        })
                        ButtonTask(text = "Добавить") {
                            val groupToSave = Group(
                                id = UUID.randomUUID().toString(),
                                creator = null,
                                name = nameGroup
                   )
                   viewModel.addGroup(groupToSave)
                        }
                    }
                }
            }
        }}} }


@Composable
fun GroupItem(
    group: Group,
    onClick: (String) -> Unit,
    rights: String
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onClick(group.id) }
            .fillMaxWidth()
    ) {
        Text(
            text = group.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = rights,
            fontSize = 14.sp
        )
    }
}