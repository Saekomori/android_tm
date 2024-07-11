package com.example.tm.bottom_navigation

import com.example.tm.R

sealed class BottomItem(val title: String, val iconId: Int, val route: String) {
    object TaskScreen: BottomItem("Задачи", R.drawable.ic_tasks, route = "task_screen")
    object GroupsScreen: BottomItem("Группы", R.drawable.ic_groups, route = "groups_screen")
    object SettingScreen: BottomItem("Настройки", R.drawable.ic_settings, route = "setting_screen")
}