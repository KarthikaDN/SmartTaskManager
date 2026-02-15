package com.kotlinpractice.smarttaskmanager.ui.addtask

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity

@Composable
fun AddTaskRoute(
    onBack: () -> Unit
) {
    val viewModel: AddTaskViewModel = hiltViewModel()
    val selectedTask by viewModel.selectedTask.collectAsStateWithLifecycle()
    AddTaskScreen(
        onSave = { title ->
            viewModel.addTask(title)
            onBack()
        },
        onUpdate = {task ->
            viewModel.updateTask(task)
            onBack()
        },
        task = selectedTask
    )
}
