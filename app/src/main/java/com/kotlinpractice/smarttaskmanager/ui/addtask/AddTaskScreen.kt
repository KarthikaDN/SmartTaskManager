package com.kotlinpractice.smarttaskmanager.ui.addtask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.domain.model.Task

@Composable
fun AddTaskScreen(
    onSave: (String) -> Unit,
    onUpdate:(task: Task)-> Unit,
    task: Task?
) {
    var text by remember(task) {
        mutableStateOf(task?.title ?: "")
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Task title") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (task == null) {
                    if(text.isNotBlank()){
                        onSave(text)
                    }
                }
                else{
                    onUpdate(task.copy(title = text))
                }
            }
        ) {
            Text("Save")
        }
    }
}

