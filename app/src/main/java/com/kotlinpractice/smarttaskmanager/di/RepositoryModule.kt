package com.kotlinpractice.smarttaskmanager.di

import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import com.kotlinpractice.smarttaskmanager.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule{

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository{
        return TaskRepository(dao)
    }
}