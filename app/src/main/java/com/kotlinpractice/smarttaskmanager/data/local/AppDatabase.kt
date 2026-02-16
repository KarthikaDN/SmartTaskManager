package com.kotlinpractice.smarttaskmanager.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kotlinpractice.smarttaskmanager.data.local.dao.TaskDao
import com.kotlinpractice.smarttaskmanager.data.local.entity.CategoryEntity
import com.kotlinpractice.smarttaskmanager.data.local.entity.TagEntity
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskTagCrossRef

@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class,
        TagEntity::class,
        TaskTagCrossRef::class],
    version = 2
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
}