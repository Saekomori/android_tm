package com.example.tm.entity

import com.example.tm.data.SubTask
import java.util.UUID

data class Group(
    val id: String,
    val creator: String?,
    val name: String
)