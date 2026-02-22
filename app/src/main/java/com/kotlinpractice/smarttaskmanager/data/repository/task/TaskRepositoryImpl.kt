package com.kotlinpractice.smarttaskmanager.data.repository.task

import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.mapper.categoryEntityToCategory
import com.kotlinpractice.smarttaskmanager.mapper.categoryToCategoryEntity
import com.kotlinpractice.smarttaskmanager.mapper.toDomain
import com.kotlinpractice.smarttaskmanager.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTaskDetail(taskId: Long): Flow<Task> {
        return taskDao.getTaskWithCategoryAndTags(taskId)
            .map { it.toDomain() }
    }

    override fun getTasksByCategory(categoryId: Long): Flow<List<Task>> {
        return taskDao.getTasksWithCategoryAndTagsByCategory(categoryId)
            .map { listOfTaskWithCatAndTags ->
                listOfTaskWithCatAndTags.map { taskWithCatAndTags ->
                    taskWithCatAndTags.toDomain()
                }
            }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasksWithCategoryAndTags().map { listOfTaskWithCatAndTags ->
            listOfTaskWithCatAndTags.map { taskWithCatAndTags ->
                taskWithCatAndTags.toDomain()
            }
        }
    }

    override suspend fun insertTask(
        task: Task,
        tagIds: List<Long>
    ) {
        taskDao.insertTaskWithTags(
            task = task.toEntity(),
            tagIds = tagIds
        )
    }

    override suspend fun updateTask(task: Task, tagIds: List<Long>) {
        taskDao.updateTaskWithTags(
            task = task.toEntity(),
            tagIds = tagIds
        )
    }

    override suspend fun deleteTask(taskId: Long) {
        taskDao.deleteTaskAndItsCrossRefs(taskId)
    }

    override suspend fun setTaskCompleted(taskId: Long, completed: Boolean) {
        taskDao.updateCompletion(
            taskId = taskId,
            completed = completed,
            updatedAt = System.currentTimeMillis()
        )
    }
}