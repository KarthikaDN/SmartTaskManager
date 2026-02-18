package com.kotlinpractice.smarttaskmanager.mapper

import com.kotlinpractice.smarttaskmanager.data.local.entity.TagEntity
import com.kotlinpractice.smarttaskmanager.domain.model.Tag
import java.time.Instant

fun TagEntity.tagEntityToTag(): Tag{
    return Tag(
        id = id,
        name = name,
        colorHex = colorHex,
        createdAt = Instant.ofEpochMilli(createdAt)
    )
}