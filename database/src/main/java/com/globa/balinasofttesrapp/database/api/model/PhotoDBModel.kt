package com.globa.balinasofttesrapp.database.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val url: String = "",
    val date: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)