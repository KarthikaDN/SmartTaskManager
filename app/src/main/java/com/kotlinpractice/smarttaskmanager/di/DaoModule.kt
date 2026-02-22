package com.kotlinpractice.smarttaskmanager.di

import com.kotlinpractice.smarttaskmanager.data.local.AppDatabase
import com.kotlinpractice.smarttaskmanager.data.local.dao.CategoryDao
import com.kotlinpractice.smarttaskmanager.data.local.dao.TagDao
import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule{

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao{
        return database.taskDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao{
        return database.categoryDao()
    }

    @Provides
    fun provideTagDao(database: AppDatabase): TagDao{
        return database.tagDao()
    }
}