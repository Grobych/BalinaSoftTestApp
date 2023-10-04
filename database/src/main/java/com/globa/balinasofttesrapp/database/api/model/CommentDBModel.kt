package com.globa.balinasofttesrapp.database.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val photoId: Int = 0,
    val date: Long = 0,
    val text: String = ""
)