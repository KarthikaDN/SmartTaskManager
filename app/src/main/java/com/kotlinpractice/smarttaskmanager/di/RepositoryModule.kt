package com.kotlinpractice.smarttaskmanager.di

import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import com.kotlinpractice.smarttaskmanager.data.repository.category.CategoryRepository
import com.kotlinpractice.smarttaskmanager.data.repository.category.CategoryRepositoryImpl
import com.kotlinpractice.smarttaskmanager.data.repository.tag.TagRepository
import com.kotlinpractice.smarttaskmanager.data.repository.tag.TagRepositoryImpl
import com.kotlinpractice.smarttaskmanager.data.repository.task.TaskRepository
import com.kotlinpractice.smarttaskmanager.data.repository.task.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule{

    @Binds
    @Singleton
    abstract fun bindTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindTagRepository(tagRepositoryImpl: TagRepositoryImpl): TagRepository
}