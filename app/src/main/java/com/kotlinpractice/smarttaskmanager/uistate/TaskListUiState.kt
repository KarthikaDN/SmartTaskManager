package com.kotlinpractice.smarttaskmanager.uistate

import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.ui.tasklist.SortOption
import com.kotlinpractice.smarttaskmanager.ui.tasklist.TaskSection
import com.kotlinpractice.smarttaskmanager.util.enums.SectionType
import com.kotlinpractice.smarttaskmanager.util.enums.SortType

data class TaskListUiState(
    val tasksSection: List<TaskSection> = emptyList(),
    val expandedSections: Set<SectionType> = setOf(
        SectionType.OVERDUE,
        SectionType.TODAY,
        SectionType.UPCOMING,
        SectionType.NO_DUE_DATE
    ),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Long? = null,
    val currentSort: SortOption = SortOption(SortType.DUE_DATE,
        isAscending = true,
        showCompletedTasks = true
    ),
    val isLoading: Boolean = false
)