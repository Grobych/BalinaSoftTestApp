package com.globa.balinasofttestapp.network.api.model

import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("data") val data: T
)
