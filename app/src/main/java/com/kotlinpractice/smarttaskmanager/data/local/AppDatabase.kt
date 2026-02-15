package com.kotlinpractice.smarttaskmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
}