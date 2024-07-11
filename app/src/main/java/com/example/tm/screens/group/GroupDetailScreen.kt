package com.example.tm.screens.group

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tm.data.SubTask
import com.example.tm.ui.theme.components.ButtonTask
import com.example.tm.ui.theme.components.TaskText
import com.example.tm.viewModels.GroupViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.tm.R
import com.example.tm.entity.TaskGroup
import com.example.tm.screens.task.parseDateTime
import com.example.tm.ui.theme.AppTheme
import com.example.tm.ui.theme.components.ButtonFab
import com.example.tm.ui.theme.components.CustomDateTimePicker
import com.example.tm.ui.theme.components.SubTaskAdd
import com.example.tm.ui.theme.components.SubTaskText
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    viewModel: GroupViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    groupId: String,
    navController: NavController
) {
    // State initialization
    var taskName by rememberSaveable { mutableStateOf("") }
    val subTasks = remember { mutableStateListOf<SubTask>() }
    val newSubTaskText = remember { mutableStateOf("") }
    val selectedDates = remember { mutableStateOf<List<LocalDate>>(listOf()) }
    val disabledDates = listOf(
        LocalDate.now().minusDays(7),
        LocalDate.now().minusDays(12),
        LocalDate.now().plusDays(3),
    )
    var taskDescription by remember { mutableStateOf("") }
    val state = rememberUseCaseState(visible = true)
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var dateTime by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var category by rememberSaveable { mutableStateOf("") }
    var notification by rememberSaveable { mutableStateOf(false) }
    var dateTimeNotification by rememberSaveable { mutableStateOf(LocalDate.now()) }
    val currentDateTime = LocalDateTime.now()
    val formattedDate = currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val groupExtended by viewModel.group.collectAsState()
    LaunchedEffect(groupId) {
        viewModel.getGroupById(groupId)
    }
    val sheetStateAddTask = rememberModalBottomSheetState()
    var showBottomSheetAddTask by remember { mutableStateOf(false) }
    val topBarTitle = remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    topBarTitle.value = groupExtended?.name ?: "Группа"
    AppTheme {


    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background),
        floatingActionButton = {
            ButtonFab { showBottomSheetAddTask = true }
        },
        floatingActionButtonPosition = FabPosition.End,
        topBar = {
            TopAppBar(
                title = { Text(text = topBarTitle.value) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(
                        onClick = { showBottomSheet = true }
                    ) {
                        Icon(Icons.Default.AccountBox, contentDescription = "Members")
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More Options")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                showMenu = false
                                showDialog = true
                            }
                        ) {
                            Text("Удалить")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.background
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonTask(text = "Назад") {
                        navController.popBackStack()
                    }
                }
            }
        }
    ) { innerPadding ->

        LaunchedEffect(Unit) {
            viewModel.loadTasksGroup(groupId)
        }

        val tasksGroup = viewModel.tasksGroup.collectAsState().value

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding),
        ) {
            items(tasksGroup) { task ->
                TasksGroupItem(taskGroup = task,
                    onClick = { groupId ->
                        navController.navigate("taskGroupDetail_screen/$groupId")
                    },
                    onCompletionChanged = { taskId, completed ->
                        val updatedTask = task.copy(completed = completed)
                        viewModel.updateTask(taskId, updatedTask, groupId)
                    },
                    onFavoriteChanged = { taskId, favorite ->
                        val updatedTask = task.copy(favorite = favorite)
                        viewModel.updateTask(taskId, updatedTask, groupId)
                    })
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                groupExtended?.creator?.let {
                    Text(
                        text = it,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    groupExtended?.let {
                        items(it.userEmail) { userEmail ->
                            MembersItem(userEmail = userEmail)
                        }
                    }
                }
                IconButton(
                    onClick = {
                        viewModel.getInviteToken(groupId) { token ->
                            if (token != null) {
                                val shareContent = "http://tmsharedgroup.com/join/$token"
                                shareContent(shareContent, context)
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    }

    if (showBottomSheetAddTask) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheetAddTask = false },
            sheetState = sheetStateAddTask,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
            ) {
                TaskText(
                    text = taskName,
                    placeholder = "Наименование",
                    label = "Добавить задачу",
                    onTextChanged = { newText -> taskName = newText },
                )
                subTasks.forEachIndexed { index, subTask ->
                    SubTaskText(
                        subTask = subTask,
                        onTextChanged = { newText ->
                            subTasks[index] = subTask.copy(text = newText)
                        },
                        onSelectedChange = { isSelected ->
                            subTasks[index] = subTask.copy(completed = isSelected)
                        },
                        onDelete = { subTasks.removeAt(index) }
                    )
                }
                SubTaskAdd { newSubTask ->
                    subTasks.add(SubTask(text = newSubTask, completed = false))
                }

                var date by remember { mutableStateOf<LocalDateTime?>(null) }
                CustomDateTimePicker(
                    context = context,
                    value = date,
                    onValueChange = { date = it },
                    label = "Срок выполнения",
                    placeholder = ""
                )
                errorMessage?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonTask(text = "Отмена", onClick = {
                        scope.launch { sheetStateAddTask.hide() }.invokeOnCompletion {
                            if (!sheetStateAddTask.isVisible) {
                                showBottomSheetAddTask = false
                            }
                        }
                    })
                    ButtonTask(text = "Сохранить") {
                        if (taskName.isEmpty() || date == null) {
                            errorMessage = "Заполните все обязательные поля."
                        } else {
                            val taskToSave = TaskGroup(
                                id = UUID.randomUUID().toString(),
                                groupId = groupId,
                                title = taskName,
                                subtasks = subTasks.toList(),
                                dateTime = date.toString(),
                                description = taskDescription,
                                category = category,
                                completed = false,
                                notification = false,
                                dateTimeNotification = null,
                                creationDate = formattedDate,
                            )
                            viewModel.addTask(taskToSave, groupId)
                            scope.launch { sheetStateAddTask.hide() }.invokeOnCompletion {
                                if (!sheetStateAddTask.isVisible) {
                                    showBottomSheetAddTask = false
                                }
                            }
                            date = null
                            taskName = ""
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteGroup(groupId)
                    showDialog = false
                    navController.popBackStack()
                }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            },
            title = { Text("Подтвердите удаление") },
            text = { Text("Вы уверены что хотите удалить эту задачу?") }
        )
    }
}}

@SuppressLint("RestrictedApi")
fun shareContent(content: String, context: Context) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, content)
    }
    val shareIntent = Intent.createChooser(intent, null)
    context.startActivity(shareIntent)
}

@Composable
fun MembersItem(userEmail: String) {
    Text(
        text = userEmail,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(8.dp),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun TasksGroupItem(
    taskGroup: TaskGroup,
    onClick: (String) -> Unit,
    onCompletionChanged: (String, Boolean) -> Unit,
    onFavoriteChanged: (String, Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick(taskGroup.id) }
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        RadioButton(
            selected = taskGroup.completed,
            onClick = { onCompletionChanged(taskGroup.id, !taskGroup.completed) }
        )
        Column {
            Text(
                text = taskGroup.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = if (taskGroup.completed) TextDecoration.LineThrough else TextDecoration.None,
                color = MaterialTheme.colorScheme.onBackground
            )
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            if (taskGroup.dateTime != null) {
                val taskDateTime = parseDateTime(taskGroup.dateTime)
                Text(
                    text = "${taskDateTime?.format(formatter)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onFavoriteChanged(taskGroup.id, !taskGroup.favorite) },
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Icon(
                painter = if (taskGroup.favorite) painterResource(id = R.drawable.ic_favorite) else painterResource(id = R.drawable.ic_favorite_border),
                contentDescription = if (taskGroup.favorite) "Remove from favorites" else "Add to favorites",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

