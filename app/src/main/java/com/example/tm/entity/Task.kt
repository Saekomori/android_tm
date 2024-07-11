package com.example.tm.entity

import com.example.tm.data.SubTask
import java.time.LocalDateTime
import java.util.UUID


data class Task(
    val id: String,
    val title: String,
    val subtasks: List<SubTask>? = null,
    val dateTime: String,
    val description: String? = null,
    val category: String? = null,
    val completed: Boolean = false,
    val notification: Boolean = false,
    val dateTimeNotification: String? = null,
    val favorite: Boolean = false,
    val creationDate: String? = null
)