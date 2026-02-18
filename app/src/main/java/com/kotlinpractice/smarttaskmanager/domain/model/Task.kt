package com.kotlinpractice.smarttaskmanager.domain.model

import com.kotlinpractice.smarttaskmanager.util.enums.Priority
import java.time.Instant

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val category: Category,
    val tags: List<Tag>,
    val priority: Priority,
    val dueDate: Instant?,
    val createdAt: Instant,
    val updatedAt: Instant
)
