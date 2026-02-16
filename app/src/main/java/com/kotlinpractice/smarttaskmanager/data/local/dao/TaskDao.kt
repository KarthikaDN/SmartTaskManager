package com.kotlinpractice.smarttaskmanager.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskEntity
import com.kotlinpractice.smarttaskmanager.data.local.entity.TaskTagCrossRef
import com.kotlinpractice.smarttaskmanager.data.local.relation.TaskWithCategoryAndTags
import com.kotlinpractice.smarttaskmanager.ui.tasklist.FilterType
import com.kotlinpractice.smarttaskmanager.ui.tasklist.SortType
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao{

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>
    //This returns a Flow. Executes only when collected. Even though Room may take time to
    //load tasks, no need to mark this as suspend Because “Flow collection is suspend, and stateIn internally launches a coroutine that collects the Flow.”

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTask(id:Long): Flow<TaskEntity>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Long)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("""
        SELECT * FROM tasks
        WHERE title LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN :sortType = "1" THEN title END ASC,
            CASE WHEN :sortType = "0" THEN title END DESC
    """)
    fun searchTasks(query: String,sortType: String): Flow<List<TaskEntity>>

    @Query("""
        SELECT * FROM tasks
        WHERE isCompleted = :isCompleted AND title LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN :sortType = "1" THEN title END ASC,
            CASE WHEN :sortType = "0" THEN title END DESC
        """)
    fun filterTasks(query: String,isCompleted: Boolean,sortType: String): Flow<List<TaskEntity>>

    //Phase 4
    @Query("""
        SELECT * FROM tasks
        WHERE categoryId = :categoryId
        ORDER BY createdAt DESC
    """)
    fun getTasksByCategory(categoryId: Long): Flow<List<TaskEntity>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskWithCategoryAndTags(taskId: Long): Flow<TaskWithCategoryAndTags>

    @Insert
    suspend fun insertTask(task: TaskEntity): Long

    @Transaction
    suspend fun insertTaskWithTags(task: TaskEntity, tagIds: List<Long>) {
        val taskId = insertTask(task)

        val crossRefs = tagIds.map { tagId ->
            TaskTagCrossRef(
                taskId = taskId,
                tagId = tagId
            )
        }

        insertTaskTagCrossRefs(crossRefs)
    }

    @Insert
    suspend fun insertTaskTagCrossRefs(
        crossRefs: List<TaskTagCrossRef>
    )


}