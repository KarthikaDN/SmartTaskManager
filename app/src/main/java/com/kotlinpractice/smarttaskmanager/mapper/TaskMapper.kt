package com.kotlinpractice.smarttaskmanager.mapper

import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.domain.model.Task

fun TaskEntity.taskEntityToTask():Task{
    return Task(
        id = id,
        title = title,
        isCompleted = isCompleted
    )
}

fun Task.taskToTaskEntity(): TaskEntity{
    return TaskEntity(
        id = id,
        title = title,
        isCompleted = isCompleted
    )
}