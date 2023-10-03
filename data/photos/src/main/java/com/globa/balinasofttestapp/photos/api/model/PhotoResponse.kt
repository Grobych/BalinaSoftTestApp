package com.globa.balinasofttestapp.photos.api.model

data class PhotoResponse(
    val id: Int,
    val url: String,
    val date: Long,
    val latitude: Double,
    val longitude: Double
)
