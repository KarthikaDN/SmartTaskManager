package com.kotlinpractice.smarttaskmanager.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kotlinpractice.smarttaskmanager.ui.addedittask.AddEditTaskRoute
import com.kotlinpractice.smarttaskmanager.ui.addedittask.AddEditTaskViewModel
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
                } ,
                onUpdateClick = {selectedId ->
                    navController.navigate(
                        "${Destinations.ADD_EDIT_TASK}/${selectedId}"
                    )
                }
            )
        }

        composable(Destinations.ADD_TASK) {
            AddEditTaskRoute(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Destinations.ADD_EDIT_TASK_ROUTE,
            arguments = listOf(
            navArgument(Destinations.TASK_ID_ARG) {
                type = NavType.LongType
                defaultValue = -1L
            }))
        {
            AddEditTaskRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }

    }
}