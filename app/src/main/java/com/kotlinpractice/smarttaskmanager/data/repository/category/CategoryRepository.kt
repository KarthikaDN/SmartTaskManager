package com.kotlinpractice.smarttaskmanager.data.repository.category

import com.kotlinpractice.smarttaskmanager.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun insertCategory(category: Category)

    fun getAllCategories(): Flow<List<Category>>
}