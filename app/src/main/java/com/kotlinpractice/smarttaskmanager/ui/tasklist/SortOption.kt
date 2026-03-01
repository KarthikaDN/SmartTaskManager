package com.kotlinpractice.smarttaskmanager.ui.tasklist

import com.kotlinpractice.smarttaskmanager.util.enums.SortType

data class SortOption(
    val type: SortType,
    val isAscending: Boolean,
    val showCompletedTasks: Boolean
)