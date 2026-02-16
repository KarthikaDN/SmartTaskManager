package com.kotlinpractice.smarttaskmanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kotlinpractice.smarttaskmanager.util.enums.Priority

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("categoryId"),
        Index("dueDate"),
        Index("priority"),
        Index("isCompleted")
    ]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val description: String?,
    val isCompleted: Boolean,

    val dueDate: Long?,
    val priority: Priority,

    val categoryId: Long,

    val createdAt: Long,
    val updatedAt: Long
)
