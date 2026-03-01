package com.kotlinpractice.smarttaskmanager.util.enums

enum class SortType {
    DUE_DATE,
    CREATED_DATE,
    TITLE,
    PRIORITY
}

fun SortType.displayText(): String{
    return when(this){
        SortType.DUE_DATE -> "Due Date"
        SortType.CREATED_DATE -> "Created Date"
        SortType.TITLE -> "Title"
        SortType.PRIORITY -> "Priority"
    }
}