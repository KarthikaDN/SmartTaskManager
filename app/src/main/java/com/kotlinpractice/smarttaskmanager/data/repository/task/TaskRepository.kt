package com.kotlinpractice.smarttaskmanager.data.repository.task

import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    /**
     * Get tasks filtered by category.
     */
    fun getTasksByCategory(categoryId: Long): Flow<List<Task>>

    fun getAllTasks(): Flow<List<Task>>

    /**
     * Get a single task with full details
     * (category + tags).
     */
    fun getTaskDetail(taskId: Long): Flow<Task?>

    /**
     * Insert a new task with selected tags.
     */
    suspend fun insertTask(task: Task, tagIds: List<Long>)

    /**
     * Update an existing task and its tags.
     */
    suspend fun updateTask(task: Task, tagIds: List<Long>)

    /**
     * Delete a task.
     */
    suspend fun deleteTask(taskId: Long)

    /**
     * Mark task as completed/uncompleted.
     */
    suspend fun setTaskCompleted(taskId: Long, completed: Boolean)
}