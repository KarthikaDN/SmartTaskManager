package com.kotlinpractice.smarttaskmanager.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinpractice.smarttaskmanager.data.repository.category.CategoryRepository
import com.kotlinpractice.smarttaskmanager.data.repository.task.TaskRepository
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.ui.events.TaskListEvent
import com.kotlinpractice.smarttaskmanager.uistate.TaskListUiState
import com.kotlinpractice.smarttaskmanager.util.enums.SectionType
import com.kotlinpractice.smarttaskmanager.util.enums.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    categoryRepository: CategoryRepository
)
    : ViewModel() {
    //Phase 4-------------------------------
    private var recentlyDeletedTasks: MutableList<Task> = mutableListOf()

    //Category selection
    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId = _selectedCategoryId
    fun selectCategory(id:Long?){
        _selectedCategoryId.value = id
    }

    //Sort Type
    private val _sortType = MutableStateFlow(SortOption(
        SortType.DUE_DATE,
        isAscending = true,
        showCompletedTasks = true
    ))
    fun updateSort(type: SortType) {
        _sortType.update { it.copy(type = type) }
    }
    fun toggleSortDirection() {
        _sortType.update { it.copy(isAscending = !it.isAscending) }
    }
    fun toggleShowCompletedTasks(){
        _sortType.update { it.copy(showCompletedTasks = !it.showCompletedTasks) }
    }

    //Expanded sections StateFlow
    private val _expandedSections = MutableStateFlow(setOf(
        SectionType.OVERDUE,
        SectionType.TODAY,
        SectionType.UPCOMING,
        SectionType.NO_DUE_DATE
    ))
    fun toggleSection(section: SectionType) {
        _expandedSections.update { current ->
            if (current.contains(section)) {
                current - section
            } else {
                current + section
            }
        }
    }


    //Ui Events
    private val _uiEvent = MutableSharedFlow<TaskListEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    //Categories StateFlow
    val categoriesFlow: StateFlow<List<Category>> =
        categoryRepository.getAllCategories().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    //Tasks StateFlow
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksFlow: StateFlow<List<Task>> =
        selectedCategoryId.flatMapLatest { categoryId ->
            categoryId?.let {
                taskRepository.getTasksByCategory(it)
            }?: taskRepository.getAllTasks()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // TaskList Ui State
    @OptIn(ExperimentalCoroutinesApi::class)
    val taskListUiState: StateFlow<TaskListUiState> = combine(
        categoriesFlow,
        tasksFlow,
        _sortType,
        _expandedSections
    ) { categories,tasks,sort,expandedSections ->

        val filtered = tasks.filter { sort.showCompletedTasks || !it.isCompleted }

        val sorted = when (sort.type) {
            SortType.DUE_DATE -> filtered.sortedBy { it.dueDate }
            SortType.CREATED_DATE -> filtered.sortedBy { it.createdAt }
            SortType.TITLE -> filtered.sortedBy { it.title.lowercase() }
            SortType.PRIORITY -> filtered.sortedBy { it.priority }
        }.let {
            if (sort.isAscending) it else it.reversed()
        }

        TaskListUiState(
            tasksSection = buildSections(sorted),
            expandedSections = expandedSections,
            categories = categories,
            selectedCategoryId = selectedCategoryId.value,
            currentSort = sort,
            isLoading = false
        )

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TaskListUiState()
    )

    //Build sections
    private fun buildSections(tasks: List<Task>): List<TaskSection> {

        val sections = mutableListOf<TaskSection>()

        val overdue = tasks.filter { it.dueDate != null && it.dueDate.isOverdue() }
        val today = tasks.filter { it.dueDate != null && it.dueDate.isToday() }
        val upcoming = tasks.filter { it.dueDate != null &&
            !it.dueDate.isOverdue() && !it.dueDate.isToday()
        }
        val noDue = tasks.filter { it.dueDate == null }

        if (overdue.isNotEmpty()) {
            sections.add(TaskSection.Header(SectionType.OVERDUE,overdue.count()))
            if(_expandedSections.value.contains(SectionType.OVERDUE)){
                overdue.forEach { sections.add(TaskSection.Item(it)) }
            }
        }

        if (today.isNotEmpty()) {
            sections.add(TaskSection.Header(SectionType.TODAY,today.count()))
            if(_expandedSections.value.contains(SectionType.TODAY)){
                today.forEach { sections.add(TaskSection.Item(it)) }
            }
        }

        if (upcoming.isNotEmpty()) {
            sections.add(TaskSection.Header(SectionType.UPCOMING,upcoming.count()))
            if(_expandedSections.value.contains(SectionType.UPCOMING)){
                upcoming.forEach { sections.add(TaskSection.Item(it)) }
            }
        }

        if(noDue.isNotEmpty()){
            sections.add(TaskSection.Header(SectionType.NO_DUE_DATE,noDue.count()))
            if(_expandedSections.value.contains(SectionType.NO_DUE_DATE)){
                noDue.forEach { sections.add(TaskSection.Item(it)) }
            }
        }
        return sections
    }

    //Delete task
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

    //Restore deleted task
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