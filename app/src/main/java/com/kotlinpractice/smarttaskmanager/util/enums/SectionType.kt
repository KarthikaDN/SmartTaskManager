package com.kotlinpractice.smarttaskmanager.util.enums

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class SectionType {
    OVERDUE,
    TODAY,
    UPCOMING,
    NO_DUE_DATE
}

fun SectionType.displayText(count: Int): String{
    return when(this){
        SectionType.OVERDUE -> "Overdue (${count})"
        SectionType.TODAY -> "Today (${count})"
        SectionType.UPCOMING -> "Upcoming (${count})"
        SectionType.NO_DUE_DATE -> "No Due Date (${count})"
    }
}

@Composable
fun SectionType.backgroundColor(): Color{
    return when(this){
        SectionType.OVERDUE -> MaterialTheme.colorScheme.errorContainer
        SectionType.TODAY -> MaterialTheme.colorScheme.primaryContainer
        SectionType.UPCOMING -> MaterialTheme.colorScheme.tertiaryContainer
        SectionType.NO_DUE_DATE -> MaterialTheme.colorScheme.surfaceVariant
    }
}

@Composable
fun SectionType.textColor(): Color{
    return when(this){
        SectionType.OVERDUE -> MaterialTheme.colorScheme.onErrorContainer
        SectionType.TODAY -> MaterialTheme.colorScheme.onPrimaryContainer
        SectionType.UPCOMING -> MaterialTheme.colorScheme.onTertiaryContainer
        SectionType.NO_DUE_DATE -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}