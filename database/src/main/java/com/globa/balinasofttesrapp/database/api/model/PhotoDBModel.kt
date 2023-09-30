package com.globa.balinasofttesrapp.database.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoDBModel(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val url: String,
    val date: Long,
    val latitude: Double,
    val longitude: Double
)