package com.example.tm.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tm.entity.Task
import com.example.tm.retrofit.task.TaskService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task = _task.asStateFlow()
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            val token = TokenManager.token
            val response = TaskService.taskApi.allTasks("Bearer $token")
            if (response.isSuccessful) {
                Log.d("test", "успешно")
                 _tasks.value = response.body() ?: emptyList()
            } else {

            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = TokenManager.token
            try {
                val response = TaskService.taskApi.addTask("Bearer $token", task)
                if (response.isSuccessful) {
                    // Задача успешно добавлена, обновить UI или список задач
                    loadTasks()  // Заново загрузить задачи, если это необходимо
                } else {
                    // Ошибка запроса
                }
            } catch (e: Exception) {
                // Ошибка сети
            }
        }
    }

    fun getTaskById(taskId: String) {
        viewModelScope.launch {
            try {
                val response = TaskService.taskApi.getTaskById(taskId)
                if (response.isSuccessful) {
                    _task.value = response.body()
                } else {
                    // Обработка ошибок HTTP
                }
            } catch (e: Exception) {
                // Обработка сетевых ошибок или других исключений
            }
        }
    }

    fun updateTask(
        taskId: String,
        task: Task
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = TaskService.taskApi.updateTask(taskId, task)
                if (response.isSuccessful) {
                    loadTasks()
                } else {
                    // Ошибка запроса
                }
            } catch (e: Exception) {
                // Ошибка сети
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = TaskService.taskApi.deleteTask(taskId)
                if (response.isSuccessful) {
                    // Успешно удалено, обновить список задач
                    loadTasks()  // Заново загрузить задачи
                } else {
                    // Обработка ошибки
                }
            } catch (e: Exception) {
                // Обработка сетевой ошибки
            }
        }
    }



}