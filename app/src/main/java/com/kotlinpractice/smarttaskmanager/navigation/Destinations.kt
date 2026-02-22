package com.kotlinpractice.smarttaskmanager.navigation

object Destinations {
    const val TASK_LIST = "task_list"
    const val ADD_TASK = "add_task"
    const val ADD_EDIT_TASK = "add_edit_task"
    const val TASK_ID_ARG = "taskId"

    const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_TASK/{$TASK_ID_ARG}"
}