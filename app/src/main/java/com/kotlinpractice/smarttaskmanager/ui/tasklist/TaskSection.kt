package com.kotlinpractice.smarttaskmanager.ui.tasklist

import com.kotlinpractice.smarttaskmanager.domain.model.Task
import com.kotlinpractice.smarttaskmanager.util.enums.SectionType

sealed class TaskSection {
    data class Header(val title: SectionType, val count: Int) : TaskSection()
    data class Item(val task: Task) : TaskSection()
}