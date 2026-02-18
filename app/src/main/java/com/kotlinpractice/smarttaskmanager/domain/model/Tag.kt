package com.kotlinpractice.smarttaskmanager.domain.model

import java.time.Instant

data class Tag(
    val id: Long,
    val name: String,
    val colorHex: String,
    val createdAt: Instant
)