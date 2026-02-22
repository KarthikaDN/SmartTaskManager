package com.kotlinpractice.smarttaskmanager.uistate

import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Tag
import com.kotlinpractice.smarttaskmanager.util.enums.Priority
import java.time.Instant

data class AddEditTaskUiState(
    val taskId: Long? = null,
    val title: String = "",
    val description: String = "",
    val selectedCategoryId: Long? = null,
    val selectedTagIds: Set<Long> = emptySet(),
    val priority: Priority = Priority.LOW,
    val dueDate: Long? = null,
    val isCompleted: Boolean = false,
    val categories: List<Category> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)