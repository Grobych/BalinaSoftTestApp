package com.globa.balinasofttestapp.photos.api.model

data class UploadPhoto(
    val base64Image: String, //Base64::class ?
    val date: Long,
    val latitude: Double,
    val longitude: Double
)