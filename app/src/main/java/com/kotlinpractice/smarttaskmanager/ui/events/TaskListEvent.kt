package com.kotlinpractice.smarttaskmanager.ui.events

sealed class TaskListEvent {
    data class ShowUndoSnackbar(val message: String) : TaskListEvent()
}