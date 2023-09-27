package com.globa.balinasofttestapp.network.api.model

import com.google.gson.annotations.SerializedName

data class Resource<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("data") val body: T
)

data class ErrorResource(
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String
)

