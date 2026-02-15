package com.kotlinpractice.smarttaskmanager.ui.tasklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.query
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity

@Composable
fun TaskListRoute(
    onAddTaskClick: () -> Unit,
    onUpdateTaskClick:(id: Long)-> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val uiState by viewModel.taskListUiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filterType by viewModel.filterState.collectAsStateWithLifecycle()
    val sortState by viewModel.sortState.collectAsStateWithLifecycle()

    TaskListScreen(
        uiState = uiState,
        onAddTaskClick = onAddTaskClick,
        onDeleteTask = {id ->
            viewModel.deleteTask(id)
        },
        onUpdateTask = {id ->
            onUpdateTaskClick(id)
        },
        onComplete = { task ->
            viewModel.markAsComplete(task)
        },
        searchText = searchText,
        onSearch = { query->
            viewModel.updateSearch(query)
        },
        filterItem = filterType,
        onFilter = {selectedItem ->
            viewModel.updateFilter(selectedItem)
        },
        sortType = sortState,
        onSort = {sortType ->
            viewModel.updateSortState(sortType)
        }

    )
}
