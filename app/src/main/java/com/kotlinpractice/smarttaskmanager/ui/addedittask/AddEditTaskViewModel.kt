package com.kotlinpractice.smarttaskmanager.ui.addedittask

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinpractice.smarttaskmanager.data.repository.category.CategoryRepository
import com.kotlinpractice.smarttaskmanager.data.repository.tag.TagRepository
import com.kotlinpractice.smarttaskmanager.data.repository.task.TaskRepository
import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.uistate.AddEditTaskUiState
import com.kotlinpractice.smarttaskmanager.util.enums.Priority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val tagRepository: TagRepository,
    private val categoryRepository: CategoryRepository,
    private val taskRepository: TaskRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var taskId: Long? = null
    init {
        initialLoad()
    }

    fun initialLoad(){
        taskId = savedStateHandle.get<Long>("taskId")?.takeIf { it != -1L }
        viewModelScope.launch {
            combine(
                categoryRepository.getAllCategories(),
                tagRepository.getAllTags()
            ) { categories, tags ->
                categories to tags
            }.collect { (categories,tags) ->
                _uiState.update {
                    it.copy(categories = categories, tags = tags)
                }
                taskId?.let { loadUpdateDetails(it) }
            }
        }
    }
    fun loadUpdateDetails(id:Long){
        viewModelScope.launch {
            val task = taskRepository.getTaskDetail(id).first()
            task?.let {
                _uiState.update { it.copy(
                    taskId = task.id,
                    title = task.title,
                    description = task.description?:"",
                    selectedCategoryId = it.categories.firstOrNull{category ->
                        category.name == task.category.name
                    }?.id,
                    selectedTagIds = it.tags.filter { tag ->
                        task.tags.contains(tag)
                    }.map { filteredTags -> filteredTags.id }.toSet(),
                    createdAt = task.createdAt.toEpochMilli(),
                    dueDate = task.dueDate?.toEpochMilli(),
                    isCompleted = task.isCompleted
                ) }
            }
        }

    }

    private val _uiState = MutableStateFlow(AddEditTaskUiState())
    val uiState: StateFlow<AddEditTaskUiState> = _uiState.asStateFlow()

    // ------------------------------------------------
    // Events
    // ------------------------------------------------

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onCategorySelected(categoryId: Long) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun onTagToggle(tagId: Long) {
        val current = _uiState.value.selectedTagIds.toMutableSet()

        if (current.contains(tagId)) {
            current.remove(tagId)
        } else {
            current.add(tagId)
        }

        _uiState.update {
            it.copy(selectedTagIds = current)
        }
    }

    fun onPrioritySelected(priority: Priority){
        _uiState.update {
            it.copy(priority = priority)
        }
    }

    fun onDueDateSelected(timestamp: Long) {
        _uiState.update { it.copy(dueDate = timestamp) }
    }

    fun onCompletionToggle() {
        _uiState.update {
            it.copy(isCompleted = !it.isCompleted)
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    // ------------------------------------------------
    // Save Logic
    // ------------------------------------------------

    fun onSaveClick() {
        val state = _uiState.value

        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "Title cannot be empty") }
            return
        }

        val selectedCategory = state.categories
            .firstOrNull { it.id == state.selectedCategoryId }

        if (selectedCategory == null) {
            _uiState.update { it.copy(error = "Please select a category") }
            return
        }

        val selectedTags = state.tags
            .filter { state.selectedTagIds.contains(it.id) }

        val now = Instant.now()

        val task = Task(
            id = state.taskId ?: 0L,
            title = state.title.trim(),
            description = state.description?.trim(),
            isCompleted = state.isCompleted,
            category = selectedCategory,
            tags = selectedTags,
            priority = state.priority, // adjust if dynamic
            dueDate = state.dueDate?.let { Instant.ofEpochMilli(it) },
            createdAt = state.createdAt?.let { Instant.ofEpochMilli(it) }?:now,
            updatedAt = now
        )

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            try {

                if (state.taskId == null) {
                    taskRepository.insertTask(
                        task = task,
                        tagIds = selectedTags.map { it.id }
                    )
                } else {
                    taskRepository.updateTask(
                        task = task,
                        tagIds = selectedTags.map { it.id }
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }
}