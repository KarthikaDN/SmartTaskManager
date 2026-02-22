package com.kotlinpractice.smarttaskmanager.ui.addedittask

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddEditTaskRoute(
    onNavigateBack: () -> Unit,
    viewModel: AddEditTaskViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isSaved) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
    }

    AddEditTaskScreen(
        state = state,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onCategorySelected = viewModel::onCategorySelected,
        onTagToggle = viewModel::onTagToggle,
        onDueDateSelected = viewModel::onDueDateSelected,
        onSaveClick = viewModel::onSaveClick,
        onBackClick = onNavigateBack,
        onPrioritySelected = viewModel::onPrioritySelected
    )
}