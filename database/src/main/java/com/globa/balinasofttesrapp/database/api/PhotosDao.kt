package com.globa.balinasofttesrapp.database.api

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globa.balinasofttesrapp.database.api.model.PhotoDBModel
import com.globa.balinasofttesrapp.database.api.model.PhotoLocationDBModel
import com.globa.balinasofttesrapp.database.api.model.PhotosRemoteKey
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotosDao {
    @Query("select * from photos")
    fun getPhotos(): PagingSource<Int,PhotoDBModel>
    @Query("select * from photos where id = :id")
    fun getPhotoById(id: Int): Flow<PhotoDBModel>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: PhotoDBModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photo : List<PhotoDBModel>)
    @Delete
    fun removePhoto(photo: PhotoDBModel): Int
    @Query("select id, latitude, longitude from photos")
    fun getPhotoLocations(): Flow<List<PhotoLocationDBModel>>
    @Query("DELETE FROM photos")
    fun clearAll()
}

@Dao
interface PhotosRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(keys: List<PhotosRemoteKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: PhotosRemoteKey)

    @Query("select * from photos_remote_keys where id=:key")
    suspend fun getKeyByPhoto(key: String): PhotosRemoteKey?

    @Query("delete from photos_remote_keys")
    suspend fun clearKeys()
}