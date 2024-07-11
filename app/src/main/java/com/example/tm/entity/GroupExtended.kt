package com.example.tm.entity

data class GroupExtended(
    val id: String,
    val creator: String?,
    val inviteToken: String,
    val name: String,
    val userEmail: List<String>,
    val tasksGroup: List<TaskGroup>
)