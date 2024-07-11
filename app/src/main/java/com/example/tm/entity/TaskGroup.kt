package com.example.tm.entity

import com.example.tm.data.SubTask

data class TaskGroup(
    val id: String,
    val groupId: String,
    val title: String,
    val subtasks: List<SubTask>? = null,
    val dateTime: String? = null,
    val description: String? = null,
    val category: String? = null,
    val completed: Boolean = false,
    val notification: Boolean = false,
    val dateTimeNotification: String? = null,
    val favorite: Boolean = false,
    val creationDate: String,
    val executor: String? = null
)
