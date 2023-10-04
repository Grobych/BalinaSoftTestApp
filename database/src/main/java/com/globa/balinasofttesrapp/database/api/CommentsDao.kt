package com.globa.balinasofttesrapp.database.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.globa.balinasofttesrapp.database.api.model.CommentDBModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComments(users: List<CommentDBModel>)

    @Query("SELECT * FROM comments WHERE photoId = :imageId")
    fun getComments(imageId: Int): Flow<List<CommentDBModel>>

    @Query("DELETE FROM comments")
    suspend fun clearAll()

    @Delete
    suspend fun deleteComment(comment: CommentDBModel): Int
}