//package com.example.tm.viewModels
//
//import TokenManager
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.tm.entity.Group
//import com.example.tm.entity.GroupExtended
//import com.example.tm.entity.Task
//import com.example.tm.entity.TaskGroup
//import com.example.tm.retrofit.group.GroupService
//import com.example.tm.retrofit.task.InviteGroup
//import com.example.tm.retrofit.task.TaskService
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.launch
//import retrofit2.Response
//
//class TasksGroupViewModel : ViewModel() {
//
//    private val _taskGroup = MutableStateFlow<TaskGroup?>(null)
//    val taskGroup = _taskGroup.asStateFlow()
//
//    private val _tasksGroup = MutableStateFlow<List<TaskGroup>>(emptyList())
//    val tasksGroup = _tasksGroup.asStateFlow()
//
//
//    init {
//
//    }
//
//    fun loadTasksGroup(groupId: String) {
//        viewModelScope.launch {
//            val response = GroupService.groupApi.getAllTasksGroup(groupId)
//            if (response.isSuccessful) {
//                _tasksGroup.value = response.body() ?: emptyList()
//            } else {
//
//            }
//        }
//    }
//
//    fun addTask(taskGroup: TaskGroup) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = GroupService.groupApi.addGroupTask(taskGroup)
//                if (response.isSuccessful) {
//                    // Задача успешно добавлена, обновить UI или список задач
//                    loadTasks()  // Заново загрузить задачи, если это необходимо
//                } else {
//                    // Ошибка запроса
//                }
//            } catch (e: Exception) {
//                // Ошибка сети
//            }
//        }
//    }
//}