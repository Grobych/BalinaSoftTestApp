package com.globa.balinasofttesrapp.database.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoDBModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "url") val url: String = "",
    @ColumnInfo(name = "date") val date: Long = 0,
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0
)

data class PhotoLocationDBModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0
)

@Entity(
    tableName = "photos_remote_keys"
)
data class PhotosRemoteKey(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "next_page") val nextPage: Int?,
)