package com.kotlinpractice.smarttaskmanager.domain.model

enum class SortType(val displayName: String) {

    DATE_DESC("Newest First"),
    DATE_ASC("Oldest First"),
    DUE_DATE_ASC("Due Soon"),
    DUE_DATE_DESC("Due Later"),
    PRIORITY_HIGH_FIRST("High Priority"),
    PRIORITY_LOW_FIRST("Low Priority"),
    NAME_ASC("A → Z"),
    NAME_DESC("Z → A")
}

