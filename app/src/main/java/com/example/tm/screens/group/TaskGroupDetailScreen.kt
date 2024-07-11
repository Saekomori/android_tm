package com.example.tm.screens.group

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import com.example.tm.entity.TaskGroup
import com.example.tm.ui.theme.components.ButtonTask
import com.example.tm.ui.theme.components.CustomDateTimePicker
import com.example.tm.ui.theme.components.SubTaskAdd
import com.example.tm.ui.theme.components.SubTaskText
import com.example.tm.ui.theme.components.TaskText
import com.example.tm.viewModels.GroupViewModel
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition", "SuspiciousIndentation")
@Composable
fun TaskGroupDetailScreen(
    viewModel: GroupViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    taskGroupId: String,
    navController: NavController
) {
    val topBarTitle = remember { mutableStateOf("") }
    var taskName by rememberSaveable { mutableStateOf("") }
    var groupId by rememberSaveable { mutableStateOf("") }
    var showUserSelectionDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
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

    var favorite by rememberSaveable { mutableStateOf(false) }
    var completed by rememberSaveable { mutableStateOf(false) }

    var dateCompleted = rememberSaveable { mutableStateOf<LocalDateTime?>(null) }

    val context = LocalContext.current
    var executor by rememberSaveable { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var showDropdown by remember { mutableStateOf(false) }
    val groupExtended by viewModel.group.collectAsState()

    LaunchedEffect(groupId) {
        viewModel.getGroupById(groupId)
    }

    val taskGroup by viewModel.taskGroup.collectAsState()

    var dateTime by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var category by rememberSaveable { mutableStateOf("") }
    var notification by rememberSaveable { mutableStateOf(false) }
    var dateTimeNotification = remember { mutableStateOf<LocalDateTime?>(null) }
    val currentDateTime = LocalDateTime.now()
    val formattedDate = currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var test by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(taskGroupId) {
        viewModel.getTaskById(taskGroupId)
    }

    // Теперь task содержит актуальные данные, полученные из viewModel
    if (taskGroup != null && !test) {
        topBarTitle.value = taskGroup!!.title
        taskName = taskGroup!!.title
        groupId = taskGroup!!.groupId
        test = true
        taskName = taskGroup!!.title
        taskDescription = taskGroup!!.description.toString()
        subTasks.clear()
        notification = taskGroup!!.notification
        dateCompleted.value = taskGroup!!.dateTime.let { LocalDateTime.parse(it) }
        if (taskGroup!!.dateTimeNotification != null) {
            dateTimeNotification.value = taskGroup!!.dateTimeNotification.let { LocalDateTime.parse(it) }
        }
        taskGroup!!.subtasks?.let { subTasks.addAll(it) }
        favorite = taskGroup!!.favorite
        completed = taskGroup!!.completed
        if (taskGroup!!.executor != null) {
            executor = taskGroup!!.executor.toString()
        }
    } else {

    }

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
                        val taskToSave = TaskGroup(
                            id = UUID.randomUUID().toString(),
                            groupId = groupId,
                            title = taskName,
                            subtasks = subTasks.toList(),
                            dateTime = formattedDate,
                            description = taskDescription,
                            category = category,
                            completed = completed,
                            notification = dateTimeNotification.value != null,
                            dateTimeNotification = dateTimeNotification.value?.toString(),
                            creationDate = formattedDate.toString(),
                            executor = executor
                        )

                        viewModel.updateTask(taskGroupId, taskToSave, groupId)

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
            //Название задачи
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

            Column(
                modifier = Modifier.padding(start = 16.dp, end = 30.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()

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
                SubTaskAdd { newSubTask ->
                    subTasks.add(SubTask(text = newSubTask, completed = false))
                }

                Box {
                    OutlinedTextField(
                        value = executor,
                        modifier = Modifier
                            .padding(8.dp)
                                .clickable {
                            showDropdown = true
                            Log.d("ff", "успешно")
                        },
                        enabled = false,

                        onValueChange = {},
                        placeholder = { Text("Выбрать пользователя") },
                        trailingIcon = {
                            if (executor.isNotEmpty()) {
                                IconButton(onClick = { executor = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Очистить")
                                }
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            disabledBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            unfocusedLabelColor = Color.Black,
                            disabledLabelColor = Color.Black
                        ),
                    )
                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false }
                    ) {
                        groupExtended?.let { group ->
                            group.userEmail.forEach { userEmail ->
                                DropdownMenuItem(onClick = {
                                    executor = userEmail
                                    showDropdown = false
                                }) {
                                    Text(userEmail)
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    value = taskDescription,
                    onValueChange = { newText -> taskDescription = newText },
                    placeholder = { Text("Добавить заметку") },
                )
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTaskGroup(taskGroupId, groupId)
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
}
