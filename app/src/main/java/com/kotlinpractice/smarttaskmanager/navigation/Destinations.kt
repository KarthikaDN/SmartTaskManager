package com.kotlinpractice.smarttaskmanager.navigation

object Destinations {
    const val TASK_LIST = "task_list"
    const val ADD_TASK = "add_task"
    const val TASK_DETAIL = "task_detail"
    const val TASK_ID_ARG = "taskId"

    const val TASK_DETAIL_ROUTE = "$TASK_DETAIL/{$TASK_ID_ARG}"
}