package com.globa.balinasofttestapp.photos.internal.locations

import com.globa.balinasofttestapp.photos.api.PhotoLocationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface PhotoLocationsRepositoryBinding {
    @Binds
    fun bindPhotoLocationsRepository(impl: PhotoLocationsRepositoryImpl): PhotoLocationsRepository
}