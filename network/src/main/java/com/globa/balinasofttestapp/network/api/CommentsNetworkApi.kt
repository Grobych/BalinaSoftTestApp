package com.globa.balinasofttestapp.network.api

import com.globa.balinasofttestapp.network.api.model.Comment
import com.globa.balinasofttestapp.network.api.model.CommentPost
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.network.api.model.Resource
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentsNetworkApi {
    @GET("api/image/{imageId}/comment")
    suspend fun getComments(
        @Header("Access-Token") token: String,
        @Path("imageId") id: Int,
        @Query("page") page: Int
    ): NetworkResponse<Resource<List<Comment>>>

    @POST("api/image/{imageId}/comment")
    suspend fun postComment(
        @Header("Access-Token") token: String,
        @Path("imageId") id: Int,
        @Body comment: CommentPost
    ): NetworkResponse<Resource<Comment>>

    @DELETE("api/image/{imageId}/comment/{commentId}")
    suspend fun deleteComment(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Path("commentId") commentId: Int
    )
}