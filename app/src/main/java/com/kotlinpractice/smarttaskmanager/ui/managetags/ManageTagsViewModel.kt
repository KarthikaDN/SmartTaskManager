package com.kotlinpractice.smarttaskmanager.ui.managetags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlinpractice.smarttaskmanager.data.repository.tag.TagRepository
import com.kotlinpractice.smarttaskmanager.domain.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ManageTagsViewModel @Inject constructor(
    private val tagRepository: TagRepository
): ViewModel() {

    val tags: StateFlow<List<Tag>> = tagRepository.getAllTags()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun addTag(tagName:String){
        viewModelScope.launch {
            tagRepository.insertTag(
                Tag(0L,tagName,"", Instant.now())
            )
        }
    }

    fun deleteTag(tagId:Long){
        viewModelScope.launch {
            tagRepository.deleteTagById(tagId)
        }
    }
}