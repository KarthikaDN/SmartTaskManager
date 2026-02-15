package com.kotlinpractice.smarttaskmanager.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.data.repository.TaskRepository
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.uistate.TaskListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(private val taskRepository: TaskRepository): ViewModel() {

    //Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    fun updateSearch(query:String){
        _searchQuery.value = query
    }

    //Filter
    private val _filterState = MutableStateFlow(FilterType.ALL)
    val filterState = _filterState.asStateFlow()
    fun updateFilter(filterType: FilterType){
        _filterState.value = filterType
    }

    //sort
    private val _sortState = MutableStateFlow(SortType.ASC)
    val sortState = _sortState.asStateFlow()
    fun updateSortState(sortType: SortType){
        _sortState.value = sortType
    }

    val taskListUiState:StateFlow<TaskListUiState> =
        combine(
            _searchQuery,
            _filterState,
            _sortState
        ){
            searchQuery,filterType,sortType->
            Triple(searchQuery,filterType,sortType)
        }
            .flatMapLatest { (searchQuery,filterType,sortType) ->
                    when{
                        filterType == FilterType.ALL -> taskRepository.searchTasks(searchQuery,sortType)
                        else -> taskRepository.filterTasks(searchQuery,filterType,sortType)
                    }.map { tasks ->
                        TaskListUiState(tasks = tasks)
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TaskListUiState(isLoading = true)
            )


    //uiState without sort
//    val taskListUiState:StateFlow<TaskListUiState> =
//        combine(
//            _searchQuery,
//            _filterState
//        ){
//            searchQuery,filterType -> searchQuery to filterType
//        }.flatMapLatest { (searchQuery,filterType) ->
//            if(filterType == FilterType.ALL){
//                taskRepository.searchTasks(searchQuery).map { tasks ->
//                    TaskListUiState(tasks = tasks)
//                }
//            }
//            else{
//                taskRepository.filterTasks(searchQuery,filterType).map { tasks ->
//                    TaskListUiState(tasks = tasks)
//                }
//            }
//
//        }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = TaskListUiState(isLoading = true)
//            )

    /*Local search: Search cached tasks
    taskRepository.getTasks() runs when uiState is collected and cached in viewmodel.
     */
//    val taskListUiState: StateFlow<TaskListUiState> =
//        combine(
//            taskRepository.getTasks(),
//            searchQuery
//        ){
//            allTasks,query ->
//            val filtered = if(query.isBlank()) allTasks
//            else allTasks.filter { it.title.contains(query) }
//
//            TaskListUiState(tasks = filtered, isLoading = false)
//        }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = TaskListUiState(isLoading = true)
//            )
    fun deleteTask(taskId:Long){
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

    fun markAsComplete(task: Task){
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }
}