package com.kotlinpractice.smarttaskmanager.data.repository

import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import com.kotlinpractice.smarttaskmanager.data.local.entity.CategoryEntity
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
        taskDao.deleteTaskById(taskId)
    }

    override suspend fun setTaskCompleted(taskId: Long, completed: Boolean) {
        taskDao.updateCompletion(
            taskId = taskId,
            completed = completed,
            updatedAt = System.currentTimeMillis()
        )
    }

    override suspend fun insertCategory(category: Category) {
        taskDao.insertCategory(category.categoryToCategoryEntity())
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return taskDao.getAllCategories().map { listOfCategoryEntites ->
            listOfCategoryEntites.map { categoryEntity ->
                categoryEntity.categoryEntityToCategory()
            }
        }
    }
}
