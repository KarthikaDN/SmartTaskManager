package com.kotlinpractice.smarttaskmanager.data.local.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.kotlinpractice.smarttaskmanager.data.local.entity.CategoryEntity
import com.kotlinpractice.smarttaskmanager.data.local.entity.TagEntity
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskTagCrossRef

data class TaskWithCategoryAndTags(
    @Embedded
    val task: TaskEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TaskTagCrossRef::class,
            parentColumn = "taskId",
            entityColumn = "tagId"
        )
    )
    val tags:List<TagEntity>
)