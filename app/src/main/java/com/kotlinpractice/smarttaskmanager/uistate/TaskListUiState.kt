package com.kotlinpractice.smarttaskmanager.uistate

import com.kotlinpractice.smarttaskmanager.domain.model.SortType
import com.kotlinpractice.smarttaskmanager.domain.model.Task

data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val selectedCategoryId: Long? = null,
    val searchQuery: String = "",
    val sortType: SortType = SortType.DATE_DESC,
    val isLoading: Boolean = false
)
