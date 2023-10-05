package com.globa.balinasofttestapp.map

import com.globa.balinasofttestapp.photos.api.model.PhotoLocation

data class MapUiState (
    val photos: List<PhotoLocation> = emptyList()
)
