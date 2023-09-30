package com.globa.balinasofttestapp.photos.internal

import com.globa.balinasofttestapp.photos.api.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface PhotosRepositoryBinding {
    @Binds
    fun bindPhotosRepository(impl: PhotosRepositoryImpl): PhotosRepository
}