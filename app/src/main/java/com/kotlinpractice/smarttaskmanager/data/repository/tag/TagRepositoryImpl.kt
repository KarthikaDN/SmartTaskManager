package com.kotlinpractice.smarttaskmanager.data.repository.tag

import com.kotlinpractice.smarttaskmanager.data.local.dao.TagDao
import com.kotlinpractice.smarttaskmanager.domain.model.Tag
import com.kotlinpractice.smarttaskmanager.mapper.tagEntityToTag
import com.kotlinpractice.smarttaskmanager.mapper.tagToTagEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val tagDao: TagDao
): TagRepository{
    override suspend fun insertTag(tag: Tag) {
        tagDao.insertTag(tag.tagToTagEntity())
    }

    override fun insertTags(tags: List<Tag>) {
        TODO("Not yet implemented")
    }

    override fun getAllTags(): Flow<List<Tag>> {
        return tagDao.getAllTags().map { listOfTagEntity ->
            listOfTagEntity.map { tagEntity ->
                tagEntity.tagEntityToTag()
            }
        }
    }

    override fun getTagById(id: Long): Tag {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTagById(id: Long) {
        tagDao.deleteTagById(id)
    }
}