package com.globa.balinasofttesrapp.database.api

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globa.balinasofttesrapp.database.api.model.CommentDBModel
import com.globa.balinasofttesrapp.database.api.model.CommentsRemoteKey

@Dao
interface CommentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComments(users: List<CommentDBModel>)

    @Query("SELECT * FROM comments WHERE photoId = :imageId")
    fun getComments(imageId: Int): PagingSource<Int,CommentDBModel>

    @Query("DELETE FROM comments")
    suspend fun clearAll()

    @Delete
    suspend fun deleteComment(comment: CommentDBModel): Int
}

@Dao
interface CommentsRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeys(keys: List<CommentsRemoteKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(key: CommentsRemoteKey)

    @Query("select * from comments_remote_keys where id=:key")
    suspend fun getKeyByComment(key: String): CommentsRemoteKey?

    @Query("delete from comments_remote_keys")
    suspend fun clearKeys()
}