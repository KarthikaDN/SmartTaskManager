package com.kotlinpractice.smarttaskmanager.ui.tasklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.query
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity

@Composable
fun TaskListRoute(
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsStateWithLifecycle()

    TaskListScreen(
        tasks = tasks,
        categories = categories,
        selectedCategoryId = selectedCategoryId,
        onCategorySelected = viewModel::selectCategory,
        onTaskClick = { /* navigate */ },
        onAddTaskClick = { /* navigate */ }
    )
}

