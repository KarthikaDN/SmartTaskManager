package com.kotlinpractice.smarttaskmanager.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                colorHex TEXT NOT NULL,
                createdAt INTEGER NOT NULL
            )
        """.trimIndent())

        database.execSQL("""
            INSERT INTO categories (id, name, colorHex, createdAt)
            VALUES (1, 'General', '#CCCCCC', ${System.currentTimeMillis()})
        """.trimIndent())

        database.execSQL("""
            ALTER TABLE tasks
            ADD COLUMN categoryId INTEGER NOT NULL DEFAULT 1
        """.trimIndent())

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS tags (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                colorHex TEXT NOT NULL,
                createdAt INTEGER NOT NULL
            )
        """.trimIndent())

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS task_tag_cross_ref (
                taskId INTEGER NOT NULL,
                tagId INTEGER NOT NULL,
                PRIMARY KEY(taskId, tagId)
            )
        """.trimIndent())

        database.execSQL("""
            CREATE INDEX IF NOT EXISTS
            index_tasks_categoryId
            ON tasks(categoryId)
        """.trimIndent())
    }
}