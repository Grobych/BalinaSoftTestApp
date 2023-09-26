package com.globa.balinasofttestapp.network.api.model

import com.google.gson.annotations.SerializedName

data class ImageDtoIn(
    @SerializedName("base64Image") val image: String,
    @SerializedName("date") val date: Long,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
)

data class ImageDtoOut(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String,
    @SerializedName("date") val date: Long,
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
)