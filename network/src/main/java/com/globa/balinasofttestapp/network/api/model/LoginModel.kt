package com.globa.balinasofttestapp.network.api.model

import com.google.gson.annotations.SerializedName

data class SignUserDtoIn(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

data class SignUserDtoOut(
    @SerializedName("userId") val userId: Int,
    @SerializedName("login") val login: String,
    @SerializedName("token") val token: String
)