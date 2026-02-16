package com.kotlinpractice.smarttaskmanager.mapper

import com.kotlinpractice.smarttaskmanager.data.local.entity.CategoryEntity
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import java.time.Instant

fun CategoryEntity.categoryEntityToCategory():Category{
    return Category(
        id = id,
        name = name,
        colorHex = colorHex,
        createdAt = Instant.ofEpochMilli(createdAt)
    )
}

fun Category.categoryToCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        colorHex = colorHex,
        createdAt = createdAt.toEpochMilli()
    )
}