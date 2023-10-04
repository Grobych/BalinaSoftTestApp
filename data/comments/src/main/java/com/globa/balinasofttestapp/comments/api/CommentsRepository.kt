package com.globa.balinasofttestapp.comments.api

import androidx.paging.PagingData
import com.globa.balinasofttestapp.comments.api.model.Comment
import com.globa.balinasofttestapp.comments.api.model.Response
import com.globa.balinasofttestapp.comments.api.model.UploadComment
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {
    suspend fun getComments(token: String, imageId: Int): Flow<PagingData<Comment>>
    suspend fun addComment(token: String, imageId: Int, comment: UploadComment): Response<Comment>
    suspend fun removeComment(token: String, imageId: Int, commentId: Int): Response<Comment>
}