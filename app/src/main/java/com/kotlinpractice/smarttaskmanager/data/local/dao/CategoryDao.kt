package com.kotlinpractice.smarttaskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kotlinpractice.smarttaskmanager.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
}