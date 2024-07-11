package com.example.tm.screens.task

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tm.R
import com.example.tm.data.SubTask
import com.example.tm.entity.Task
import com.example.tm.ui.theme.AppTheme
import com.example.tm.ui.theme.components.ButtonTask
import com.example.tm.ui.theme.components.CustomDatePicker
import com.example.tm.ui.theme.components.CustomDateTimePicker
import com.example.tm.ui.theme.components.SubTaskAdd
import com.example.tm.ui.theme.components.SubTaskText
import com.example.tm.ui.theme.components.TaskText
import com.example.tm.viewModels.TaskViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TaskDetailScreen(
    viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    taskId: String,
    navController: NavController,
) {
    val topBarTitle = remember { mutableStateOf("") }
    var taskName by rememberSaveable { mutableStateOf("") }
    var subTasks = remember { mutableStateListOf<SubTask>() }
    var taskDescription by rememberSaveable { mutableStateOf("") }
    val task by viewModel.task.collectAsState()
    var category by rememberSaveable { mutableStateOf("") }
    var notification by rememberSaveable { mutableStateOf(false) }
    var favorite by rememberSaveable { mutableStateOf(false) }
    var completed by rememberSaveable { mutableStateOf(false) }
    var dateTimeNotification = remember { mutableStateOf<LocalDateTime?>(null) }
    var dateCompleted = rememberSaveable { mutableStateOf<LocalDateTime?>(null) }
    var dateCreation = rememberSaveable { mutableStateOf<LocalDateTime?>(null) }
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var test by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        viewModel.getTaskById(taskId)
    }

    if (task != null && !test) {
        topBarTitle.value = "Моя задача"
        taskName = task!!.title
        taskDescription = task!!.description.toString()
        subTasks.clear()
        notification = task!!.notification
        dateCompleted.value = task!!.dateTime?.let { LocalDateTime.parse(it) }
        if (task!!.dateTimeNotification != null) {
            dateTimeNotification.value = task!!.dateTimeNotification?.let { LocalDateTime.parse(it) }
        }
        task!!.subtasks?.let { subTasks.addAll(it) }
        favorite = task!!.favorite
        completed = task!!.completed
        dateCreation.value = task!!.creationDate?.let { LocalDateTime.parse(it) }
        test = true
    }
    AppTheme {


    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = { Text(text = topBarTitle.value) },
                actions = {
                    IconButton(
                        onClick = { favorite = !favorite }, // Toggle favorite status
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = if (favorite) painterResource(id = R.drawable.ic_favorite) else painterResource(id = R.drawable.ic_favorite_border),
                            contentDescription = if (favorite) "Remove from favorites" else "Add to favorites"
                        )
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
                    ButtonTask(text = "Сохранить") {
                        val taskToSave = Task(
                            id = UUID.randomUUID().toString(),
                            title = taskName,
                            subtasks = subTasks.toList(),
                            dateTime = dateCompleted.value.toString(),
                            description = taskDescription,
                            category = category,
                            completed = completed,
                            notification = dateTimeNotification.value != null,
                            dateTimeNotification = dateTimeNotification.value?.toString(),
                            favorite = favorite,
                            creationDate = dateCreation.value.toString()
                        )
                        viewModel.updateTask(taskId, taskToSave)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Task Title Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = completed, onClick = { completed = !completed })
                TaskText(
                    text = taskName,
                    placeholder = "Title",
                    label = "",
                    onTextChanged = { newText -> taskName = newText },
                    textDecoration = if (completed) TextDecoration.LineThrough else TextDecoration.None
                )
            }

            // Task Details Column
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 30.dp),
            ) {
                // Date Picker Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "", modifier = Modifier.padding(start = 14.dp, end = 10.dp))
                    CustomDateTimePicker(
                        context = context,
                        value = dateCompleted.value,
                        onValueChange = { dateCompleted.value = it },
                        label = "",
                        placeholder = "Срок выполнения"
                    )
                }

                // Notification Picker Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Icon(Icons.Filled.Notifications, contentDescription = "", modifier = Modifier.padding(start = 14.dp, end = 10.dp))
                    CustomDateTimePicker(
                        context = context,
                        value = dateTimeNotification.value,
                        onValueChange = { newValue ->
                            dateTimeNotification.value = newValue
                            notification = newValue != null
                        },
                        label = "",
                        placeholder = "Напомнить",
                        visibleDelete = true
                    )
                }

                // SubTasks
                subTasks.forEachIndexed { index, subTask ->
                    SubTaskText(
                        subTask = subTask,
                        onTextChanged = { newText ->
                            subTasks[index] = subTask.copy(text = newText)
                        },
                        onSelectedChange = { isSelected ->
                            subTasks[index] = subTask.copy(completed = isSelected)
                        },
                        onDelete = {
                            subTasks.removeAt(index)
                        }
                    )
                }

                // Add SubTask
                SubTaskAdd { newSubTask ->
                    subTasks.add(SubTask(text = newSubTask, completed = false))
                }

                // Description TextField
                OutlinedTextField(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .height(150.dp)
                        .fillMaxWidth(),
                    value = taskDescription,
                    onValueChange = { newText -> taskDescription = newText },
                    placeholder = { Text("Добавить заметку") },
                )
            }
        }
    }

    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTask(taskId)
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
            text = { Text("Вы уверены что хотите удалить задачу?") }
        )
    }
}}