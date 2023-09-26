package com.globa.balinasofttestapp.network.api

import com.globa.balinasofttestapp.network.api.model.Response
import com.globa.balinasofttestapp.network.api.model.SignUserDtoIn
import com.globa.balinasofttestapp.network.api.model.SignUserDtoOut
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginNetworkApi {
    @POST("api/account/signin")
    suspend fun signIn(@Body loginData: SignUserDtoIn): Response<SignUserDtoOut>

    @POST("api/account/signup")
    suspend fun signUp(@Body signUpData: SignUserDtoIn): Response<SignUserDtoOut>
}