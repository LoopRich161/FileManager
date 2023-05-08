package ru.looprich.filemanager.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey val path: String,
    val oldHash: String?,
    val newHash: String?
)
