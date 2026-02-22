package com.kotlinpractice.smarttaskmanager.data.repository.category

import com.kotlinpractice.smarttaskmanager.data.local.dao.CategoryDao
import com.kotlinpractice.smarttaskmanager.domain.model.Category
import com.kotlinpractice.smarttaskmanager.mapper.categoryEntityToCategory
import com.kotlinpractice.smarttaskmanager.mapper.categoryToCategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
): CategoryRepository {

    override suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category.categoryToCategoryEntity())
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { listOfCategoryEntites ->
            listOfCategoryEntites.map { categoryEntity ->
                categoryEntity.categoryEntityToCategory()
            }
        }
    }
}