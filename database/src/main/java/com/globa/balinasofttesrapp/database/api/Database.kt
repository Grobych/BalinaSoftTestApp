package com.globa.balinasofttesrapp.database.api

import androidx.room.Database
import androidx.room.RoomDatabase
import com.globa.balinasofttesrapp.database.api.model.CommentDBModel
import com.globa.balinasofttesrapp.database.api.model.CommentsRemoteKey
import com.globa.balinasofttesrapp.database.api.model.PhotoDBModel
import com.globa.balinasofttesrapp.database.api.model.PhotosRemoteKey

@Database(
    entities = [PhotoDBModel::class, CommentDBModel::class, PhotosRemoteKey::class, CommentsRemoteKey::class],
    version = 1,
    exportSchema = false
)

abstract class PhotosDatabase : RoomDatabase() {
    abstract val photosDao: PhotosDao
    abstract val commentsDao: CommentsDao
    abstract val photosRemoteKeyDao: PhotosRemoteKeyDao
    abstract val commentsRemoteKeyDao: CommentsRemoteKeyDao
}