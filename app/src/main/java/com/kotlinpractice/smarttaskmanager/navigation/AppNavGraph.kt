package com.kotlinpractice.smarttaskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kotlinpractice.smarttaskmanager.ui.addtask.AddTaskRoute
import com.kotlinpractice.smarttaskmanager.ui.addtask.AddTaskScreen
import com.kotlinpractice.smarttaskmanager.ui.tasklist.TaskListRoute
import com.kotlinpractice.smarttaskmanager.ui.tasklist.TaskListScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.TASK_LIST
    ) {
        composable(Destinations.TASK_LIST) {
            TaskListRoute(
                onAddTaskClick = {
                    navController.navigate(Destinations.ADD_TASK)
                },
                onUpdateTaskClick = {selectedId ->
                    navController.navigate(
                        "${Destinations.TASK_DETAIL}/${selectedId}"
                    )
                }
            )
        }

        composable(Destinations.ADD_TASK) {
            AddTaskRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Destinations.TASK_DETAIL_ROUTE,
            arguments = listOf(
            navArgument(Destinations.TASK_ID_ARG) {
                type = NavType.LongType
            }))
        {
            AddTaskRoute(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}