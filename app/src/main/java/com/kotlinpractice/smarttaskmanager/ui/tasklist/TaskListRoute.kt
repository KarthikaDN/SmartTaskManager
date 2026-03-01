package com.kotlinpractice.smarttaskmanager.ui.tasklist

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.query
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.ui.events.TaskListEvent
import kotlinx.coroutines.flow.collectLatest
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun TaskListRoute(
    onAddTaskClick:()-> Unit,
    onUpdateClick:(Long)-> Unit,
    onManageTagsClick:()-> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val taskListUiState by viewModel.taskListUiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    // Collect one-time events here
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is TaskListEvent.ShowUndoSnackbar -> {

                    try {
                        val result = snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.restoreDeletedTask()
                        } else {
                            viewModel.clearRecentlyDeletedTasksList()
                        }

                    } catch (e: CancellationException) {
                        // Snackbar was replaced by a newer one.
                        // DO NOT clear anything here.
                        throw e // Always rethrow cancellation
                    }
                }
            }
        }
    }
    LaunchedEffect(taskListUiState.currentSort) {
        listState.animateScrollToItem(0)
    }
    TaskListScreen(
        taskListUiState = taskListUiState,
        onCategorySelected = viewModel::selectCategory,
        listState = listState,
        onTaskClick = { id ->
            onUpdateClick(id)
        },
        onToggleComplete = viewModel::markTaskAsComplete,
        onDeleteTask = viewModel::deleteTask,
        onAddTaskClick = onAddTaskClick,
        snackbarHostState = snackbarHostState,
        onSortSelected = viewModel::updateSort,
        onToggleDirection = viewModel::toggleSortDirection,
        onSectionExpanded = viewModel::toggleSection,
        onToggleShowCompletedTasks = viewModel::toggleShowCompletedTasks,
        onManageTagsClicked = onManageTagsClick
    )
}

