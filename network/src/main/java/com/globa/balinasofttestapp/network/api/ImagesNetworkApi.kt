package com.globa.balinasofttestapp.network.api

import com.globa.balinasofttestapp.network.api.model.ImageDtoIn
import com.globa.balinasofttestapp.network.api.model.ImageDtoOut
import com.globa.balinasofttestapp.network.api.model.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ImagesNetworkApi {
    @GET("api/image")
    suspend fun getImages(
        @Header("Access-Token") token: String,
        @Query("page") page: Int
    ): Response<List<ImageDtoOut>>

    @POST("api/image")
    suspend fun postImage(
        @Header("Access-Token") token: String,
        @Body imageDtoIn: ImageDtoIn
    ): Response<ImageDtoOut>

    @DELETE("api/image/{id}")
    suspend fun deleteImage(
        @Header("Access-Token") token: String,
        @Path("id") id: Int
    ): Response<ImageDtoOut>

    @GET("api/image/{id}")
    suspend fun getImage(
        @Header("Access-Token") token: String,
        @Path("id") id: Int
    ): Response<ImageDtoOut>

}