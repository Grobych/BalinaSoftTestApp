package com.globa.balinasofttestapp.photos.internal.photos

import com.globa.balinasofttestapp.photos.api.PhotosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
internal interface PhotosRepositoryBinding {
    @Binds
    fun bindPhotosRepository(impl: PhotosRepositoryImpl): PhotosRepository
}