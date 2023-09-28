package com.globa.balinasofttestapp.location.api

interface LocationRepository {
    suspend fun getLocation(): LocationResponse
}