package com.kotlinpractice.smarttaskmanager

import androidx.compose.runtime.Composable
import com.kotlinpractice.smarttaskmanager.navigation.AppNavGraph
import com.kotlinpractice.smarttaskmanager.ui.theme.TaskManagerTheme

@Composable
fun SmartTaskManagerApp() {
    TaskManagerTheme {
        AppNavGraph()
    }
}