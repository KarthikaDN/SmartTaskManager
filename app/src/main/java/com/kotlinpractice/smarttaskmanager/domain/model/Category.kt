package com.kotlinpractice.smarttaskmanager.domain.model

import java.time.Instant

data class Category(
    val id: Long,
    val name: String,
    val colorHex: String,
    val createdAt: Instant
)

