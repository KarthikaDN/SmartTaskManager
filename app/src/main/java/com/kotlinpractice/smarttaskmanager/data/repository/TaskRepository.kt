package com.kotlinpractice.smarttaskmanager.data.repository

import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.mapper.taskEntityToTask
import com.kotlinpractice.smarttaskmanager.mapper.taskToTaskEntity
import com.kotlinpractice.smarttaskmanager.ui.tasklist.FilterType
import com.kotlinpractice.smarttaskmanager.ui.tasklist.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepository(
    private val taskDao: TaskDao
) {
    fun getTasks(): Flow<List<Task>>{
        return taskDao.getAllTasks().map { taskEntities ->
            taskEntities.map { taskEntity ->
                taskEntity.taskEntityToTask()
            }
        }
    }

    fun getTask(id:Long): Flow<Task>{
        return taskDao.getTask(id).map { taskEntity ->
            taskEntity.taskEntityToTask()
        }
    }
    suspend fun insertTask(title:String){
        taskDao.insertTask(TaskEntity(title = title))
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task.taskToTaskEntity())
    }

    suspend fun deleteTask(id:Long){
        taskDao.deleteTask(id)
    }

    suspend fun searchTasks(query:String,sortType: SortType): Flow<List<Task>>{
        return taskDao.searchTasks(query,if(sortType == SortType.ASC) "1" else "0" ).map { taskEntities ->
            taskEntities.map { taskEntity ->
                taskEntity.taskEntityToTask()
            }
        }
    }

    suspend fun filterTasks(query:String,filterType: FilterType,sortType: SortType): Flow<List<Task>>{
        return taskDao.filterTasks(query,
            filterType == FilterType.COMPLETED,
            if(sortType == SortType.ASC) "1" else "0"
            ).map { taskEntities ->
            taskEntities.map { taskEntity ->
                taskEntity.taskEntityToTask()
            }
        }
    }
}