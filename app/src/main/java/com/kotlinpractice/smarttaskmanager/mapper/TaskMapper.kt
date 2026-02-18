package com.kotlinpractice.smarttaskmanager.mapper

import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.data.local.relation.TaskWithCategoryAndTags
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import java.time.Instant

fun TaskWithCategoryAndTags.toDomain(): Task {
    return Task(
        id = task.id,
        title = task.title,
        description = task.description,
        isCompleted = task.isCompleted,
        category = category.categoryEntityToCategory(),
        tags = tags.map { it.tagEntityToTag() },
        priority = task.priority,
        dueDate = task.dueDate?.let { Instant.ofEpochMilli(it) },
        createdAt = Instant.ofEpochMilli(task.createdAt),
        updatedAt = Instant.ofEpochMilli(task.updatedAt)
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        dueDate = dueDate?.toEpochMilli(),
        priority = priority,
        categoryId = category.id,
        createdAt = createdAt.toEpochMilli(),
        updatedAt = updatedAt.toEpochMilli()
    )
}
