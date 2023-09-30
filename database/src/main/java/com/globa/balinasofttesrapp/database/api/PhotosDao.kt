package com.globa.balinasofttesrapp.database.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globa.balinasofttesrapp.database.api.model.PhotoDBModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {
    @Query("select * from photos")
    fun getPhotos(): Flow<List<PhotoDBModel>>
    @Query("select * from photos where id = :id")
    fun getPhotoById(id: Int): Flow<PhotoDBModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoDBModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photo : List<PhotoDBModel>)
    @Delete
    fun removePhoto(photo: PhotoDBModel)
}