package com.globa.balinasofttesrapp.database.api

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globa.balinasofttesrapp.database.api.model.PhotoDBModel

@Database(
    entities = [PhotoDBModel::class],
    version = 1,
    exportSchema = false
)

abstract class PhotosDatabase : RoomDatabase() {
    abstract val photosDao: PhotosDao
}