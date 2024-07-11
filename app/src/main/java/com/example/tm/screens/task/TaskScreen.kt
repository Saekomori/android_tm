package com.example.tm.screens.task

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tm.entity.Task
import com.example.tm.ui.theme.components.ButtonFab
import com.example.tm.viewModels.TaskViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import com.example.tm.R
import com.example.tm.data.SubTask
import com.example.tm.ui.theme.AppTheme
import com.example.tm.ui.theme.components.ButtonTask
import com.example.tm.ui.theme.components.CustomDatePicker
import com.example.tm.ui.theme.components.CustomDateTimePicker
import com.example.tm.ui.theme.components.SubTaskAdd
import com.example.tm.ui.theme.components.SubTaskText
import com.example.tm.ui.theme.components.TaskText
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
enum class TaskFilter(val title: String, val description: String) {
    ALL("Все задачи", "Показать все задачи"),
    FAVORITE("Избранные задачи", "Показать избранные задачи"),
    COMPLETED("Выполненные задачи", "Показать выполненные задачи"),
    NOT_COMPLETED("Невыполненные задачи", "Показать невыполненные задачи")
}

enum class SortOrder(val description: String) {
    NONE("Без сортировки"),
    BY_DATE("Сортировка по дате"),
    BY_NAME("Сортировка по имени")
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController,
) {
    val topBarTitle = remember { mutableStateOf("Все задачи") }
    var taskName by rememberSaveable { mutableStateOf("") }
    val subTasks = remember { mutableStateListOf<SubTask>() }
    val selectedDates = remember { mutableStateOf<List<LocalDate>>(listOf()) }
    val disabledDates = listOf(
        LocalDate.now().minusDays(7),
        LocalDate.now().minusDays(12),
        LocalDate.now().plusDays(3),
    )
    var taskDescription by remember { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var notification by rememberSaveable { mutableStateOf(false) }
    var dateTimeNotification by rememberSaveable { mutableStateOf(LocalDate.now()) }
    var filter by remember { mutableStateOf(TaskFilter.ALL) }
    var sortOrder by remember { mutableStateOf(SortOrder.NONE) }
    val sheetStateAddTask = rememberModalBottomSheetState()
    var showBottomSheetAddTask by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val currentDateTime = LocalDateTime.now()
    val formattedDate = currentDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    var expandedFilterMenu by remember { mutableStateOf(false) }
    var expandedSortMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }
    AppTheme {


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = topBarTitle.value) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    // Dropdown menu for filtering
                    Box {
                        IconButton(onClick = { expandedFilterMenu = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sorting),
                                contentDescription = "Filter tasks"
                            )
                        }
                        DropdownMenu(
                            expanded = expandedFilterMenu,
                            onDismissRequest = { expandedFilterMenu = false }
                        ) {
                            TaskFilter.values().forEach { filterOption ->
                                DropdownMenuItem(
                                    text = { Text(filterOption.title) },
                                    onClick = {
                                        filter = filterOption
                                        topBarTitle.value = filterOption.title
                                        expandedFilterMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Dropdown menu for sorting
                    Box {
                        IconButton(onClick = { expandedSortMenu = true }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_sorting1),
                                contentDescription = "Sort tasks"
                            )
                        }
                        DropdownMenu(
                            expanded = expandedSortMenu,
                            onDismissRequest = { expandedSortMenu = false }
                        ) {
                            SortOrder.values().forEach { sortOrderOption ->
                                DropdownMenuItem(
                                    text = { Text(sortOrderOption.description) },
                                    onClick = {
                                        sortOrder = sortOrderOption
                                        expandedSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ButtonFab { showBottomSheetAddTask = true }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { padding ->
        LaunchedEffect(Unit) {
            viewModel.loadTasks()
        }

        val tasks = viewModel.tasks.collectAsState().value

        val filteredTasks = tasks.filter {
            when (filter) {
                TaskFilter.ALL -> true
                TaskFilter.FAVORITE -> it.favorite
                TaskFilter.COMPLETED -> it.completed
                TaskFilter.NOT_COMPLETED -> !it.completed
            }
        }

        val sortedTasks = when (sortOrder) {
            SortOrder.NONE -> filteredTasks
            SortOrder.BY_DATE -> filteredTasks.sortedBy { it.dateTime }
            SortOrder.BY_NAME -> filteredTasks.sortedBy { it.title }
        }

        LazyColumn(
            modifier = Modifier.padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(sortedTasks) { task ->
                TaskItem(
                    task = task,
                    onClick = { taskId ->
                        navController.navigate("taskDetail_screen/$taskId")
                    },
                    onCompletionChanged = { taskId, completed ->
                        val updatedTask = task.copy(completed = completed)
                        viewModel.updateTask(taskId, updatedTask)
                    },
                    onFavoriteChanged = { taskId, favorite ->
                        val updatedTask = task.copy(favorite = favorite)
                        viewModel.updateTask(taskId, updatedTask)
                    }
                )
            }
        }
    }

    if (showBottomSheetAddTask) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheetAddTask = false
            },
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
                        onDelete = {
                            subTasks.removeAt(index)
                        }
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
                            val taskToSave = Task(
                                id = UUID.randomUUID().toString(),
                                title = taskName,
                                subtasks = subTasks.toList(),
                                dateTime = date.toString(),
                                description = taskDescription,
                                category = category,
                                completed = false,
                                notification = notification,
                                dateTimeNotification = null,
                                creationDate = formattedDate
                            )
                            viewModel.addTask(taskToSave)
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
}
}


@Composable
fun TaskItem(
    task: Task,
    onClick: (String) -> Unit,
    onCompletionChanged: (String, Boolean) -> Unit,
    onFavoriteChanged: (String, Boolean) -> Unit
) {
    AppTheme {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick(task.id) }
            .fillMaxWidth()
    ) {
        RadioButton(
            selected = task.completed,
            onClick = { onCompletionChanged(task.id, !task.completed) }
        )
        Column {
            Text(
                text = task.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
            )

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            task.dateTime?.let {
                Text(
                    text = LocalDateTime.parse(it).format(formatter),
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onFavoriteChanged(task.id, !task.favorite) }
        ) {
            Icon(
                painter = if (task.favorite) painterResource(id = R.drawable.ic_favorite) else painterResource(id = R.drawable.ic_favorite_border),
                contentDescription = if (task.favorite) "Remove from favorites" else "Add to favorites"
            )
        }
    }
}
}

fun parseDateTime(dateTimeString: String?): LocalDateTime? {
    return try {
        LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
    } catch (e: Exception) {
        null
    }
}