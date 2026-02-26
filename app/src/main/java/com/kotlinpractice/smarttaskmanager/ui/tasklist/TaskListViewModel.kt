package com.kotlinpractice.smarttaskmanager.ui.tasklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.data.repository.category.CategoryRepository
import com.kotlinpractice.smarttaskmanager.data.repository.task.TaskRepository
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.ui.events.TaskListEvent
import com.kotlinpractice.smarttaskmanager.uistate.TaskListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository
)
    : ViewModel() {
    //Phase 4-------------------------------
    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId = _selectedCategoryId
    fun selectCategory(id:Long?){
        _selectedCategoryId.value = id
    }

    private var recentlyDeletedTasks: MutableList<Task> = mutableListOf()

    private val _uiEvent = MutableSharedFlow<TaskListEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val categories: StateFlow<List<Category>> =
        categoryRepository.getAllCategories().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList<Category>()
        )


    val tasks: StateFlow<List<Task>> =
        selectedCategoryId.flatMapLatest { categoryId ->
            categoryId?.let {
                taskRepository.getTasksByCategory(it)
            }?: taskRepository.getAllTasks()
        }
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList<Task>()
        )

    fun deleteTask(taskId:Long){

        viewModelScope.launch {
            val task:Task? = taskRepository.getTaskDetail(taskId).first()
            task?.let {
                recentlyDeletedTasks.add(it)
                taskRepository.deleteTask(it.id)
                _uiEvent.emit(
                    TaskListEvent.ShowUndoSnackbar("${recentlyDeletedTasks.count()} Task(s) deleted")
                )
            }
        }
    }

    fun restoreDeletedTask(){
        viewModelScope.launch {
            recentlyDeletedTasks.let {
                it.forEach { task ->
                    taskRepository.insertTask(task.copy(id = 0),task.tags.map { tag -> tag.id })
                }
            }
            recentlyDeletedTasks.clear()

        }
    }

    fun clearRecentlyDeletedTasksList(){
        recentlyDeletedTasks.clear()
    }
    fun markTaskAsComplete(taskId:Long,isComplete: Boolean){
        viewModelScope.launch {
            taskRepository.setTaskCompleted(taskId,isComplete)
        }
    }
}