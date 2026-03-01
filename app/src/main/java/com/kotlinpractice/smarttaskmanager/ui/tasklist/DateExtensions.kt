package com.kotlinpractice.smarttaskmanager.ui.tasklist

import java.time.*

private fun currentDate(): LocalDate {
    return LocalDate.now(ZoneId.systemDefault())
}

fun Instant?.isToday(): Boolean {
    if (this == null) return false
    val date = this.atZone(ZoneId.systemDefault()).toLocalDate()
    return date == currentDate()
}

fun Instant?.isOverdue(): Boolean {
    if (this == null) return false
    val date = this.atZone(ZoneId.systemDefault()).toLocalDate()
    return date.isBefore(currentDate())
}