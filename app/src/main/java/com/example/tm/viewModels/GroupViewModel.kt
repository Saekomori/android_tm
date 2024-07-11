package com.example.tm.viewModels

import TokenManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tm.entity.Group
import com.example.tm.entity.GroupExtended
import com.example.tm.entity.Task
import com.example.tm.entity.TaskGroup
import com.example.tm.retrofit.group.GroupService
import com.example.tm.retrofit.task.InviteGroup
import com.example.tm.retrofit.task.TaskService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class GroupViewModel : ViewModel() {
    private val _groupsCreator = MutableStateFlow<List<Group>>(emptyList())
    val groupsCreator = _groupsCreator.asStateFlow()

    private val _groupsUser = MutableStateFlow<List<Group>>(emptyList())
    val groupsUser = _groupsUser.asStateFlow()

    private val _groupsInviteToken = MutableStateFlow(InviteGroup(""))
    val groupsInviteToken: StateFlow<InviteGroup> = _groupsInviteToken

    private val _group = MutableStateFlow<GroupExtended?>(null)
    val group = _group.asStateFlow()

    private val _taskGroup = MutableStateFlow<TaskGroup?>(null)
    val taskGroup = _taskGroup.asStateFlow()

    private val _tasksGroup = MutableStateFlow<List<TaskGroup>>(emptyList())
    val tasksGroup = _tasksGroup.asStateFlow()

    fun updateInviteToken(token: String) {
        _groupsInviteToken.value = InviteGroup(token)
    }
    init {
        loadGroupsCreator()
        loadGroupsUser()

    }

    fun loadTasksGroup(groupId: String) {
        viewModelScope.launch {
            val response = GroupService.groupApi.getAllTasksGroup(groupId)
            if (response.isSuccessful) {
                _tasksGroup.value = response.body() ?: emptyList()
            } else {

            }
        }
    }

    fun loadGroupsCreator() {
        viewModelScope.launch {
            val token = TokenManager.token
            val response = GroupService.groupApi.getGroupCreator("Bearer $token")
            if (response.isSuccessful) {
                _groupsCreator.value = response.body() ?: emptyList()
            } else {

            }
        }
    }

    fun loadGroupsUser() {
        viewModelScope.launch {
            val token = TokenManager.token
            val response = GroupService.groupApi.getGroupUser("Bearer $token")
            if (response.isSuccessful) {
                _groupsUser.value = response.body() ?: emptyList()
            } else {

            }
        }
    }

    fun getInviteToken(groupId: String, callback: (String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<InviteGroup> = GroupService.groupApi.generateInviteToken(groupId)
                if (response.isSuccessful) {
                    val inviteGroup: InviteGroup? = response.body()
                    val token: String? = inviteGroup?.inviteToken
                    if (token != null) {
                        _groupsInviteToken.value = inviteGroup
                        callback(token)
                    } else {
                        Log.e("TEST", "Token is null")
                        callback(null)
                    }
                } else {
                    Log.e("TEST", "Response unsuccessful: ${response.code()}")
                    callback(null)
                }
            } catch (e: Exception) {
                Log.e("TEST", "Error occurred while getting invite token", e)
                callback(null)
            }
        }
    }

    fun getGroupById(groupId: String) {
        viewModelScope.launch {
            try {
                val response = GroupService.groupApi.getGroupById(groupId)
                if (response.isSuccessful) {
                    _group.value = response.body()
                } else {
                    // Обработка ошибок HTTP
                }
            } catch (e: Exception) {
                // Обработка сетевых ошибок или других исключений
            }
        }
    }


    fun joinGroup(
        tokenInvite: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = TokenManager.token
            try {
                val response = GroupService.groupApi.joinGroup(tokenInvite, "Bearer $token")
                if (response.isSuccessful) {
                    // Задача успешно добавлена, обновить UI или список задач
                } else {
                    // Ошибка запроса
                }
            } catch (e: Exception) {
                // Ошибка сети
            }
        }
    }

    fun addGroup(group: Group) {
        viewModelScope.launch(Dispatchers.IO) {
            val token = TokenManager.token
            try {
                val response = GroupService.groupApi.addGroup(group, "Bearer $token")
                if (response.isSuccessful) {
                    loadGroupsCreator()
                    loadGroupsUser()
                } else {
                }
            } catch (e: Exception) {
            }
        }
    }

    fun addTask(taskGroup: TaskGroup, groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = GroupService.groupApi.addGroupTask(taskGroup)
                if (response.isSuccessful) {
                    // Задача успешно добавлена, обновить UI или список задач
                    loadTasksGroup(groupId)  // Заново загрузить задачи, если это необходимо
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
                val response = GroupService.groupApi.getTaskGroupById(taskId)
                if (response.isSuccessful) {
                    _taskGroup.value = response.body()
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
        taskGroup: TaskGroup,
        groupId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = GroupService.groupApi.updateTask(taskId, taskGroup)
                if (response.isSuccessful) {
                    loadTasksGroup(groupId)
                } else {
                    // Ошибка запроса
                }
            } catch (e: Exception) {
                // Ошибка сети
            }
        }
    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = GroupService.groupApi.deleteGroup(groupId)
                if (response.isSuccessful) {

                } else {

                }
            } catch (e: Exception) {

            }
        }
    }


    fun deleteTaskGroup(taskGroupId: String, groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = GroupService.groupApi.deleteTaskGroup(taskGroupId)
                if (response.isSuccessful) {
                    loadTasksGroup(groupId)
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }



}