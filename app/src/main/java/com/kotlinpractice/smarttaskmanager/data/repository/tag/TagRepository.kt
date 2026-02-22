package com.kotlinpractice.smarttaskmanager.data.repository.tag

import com.kotlinpractice.smarttaskmanager.data.local.entity.TagEntity
import com.kotlinpractice.smarttaskmanager.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun insertTag(tag: Tag)
    fun insertTags(tags: List<Tag>)
    fun getAllTags(): Flow<List<Tag>>
    fun getTagById(id:Long): Tag
}