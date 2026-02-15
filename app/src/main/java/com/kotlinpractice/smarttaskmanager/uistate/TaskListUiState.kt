package com.kotlinpractice.smarttaskmanager.uistate

import com.kotlinpractice.smarttaskmanager.domain.model.Task

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)