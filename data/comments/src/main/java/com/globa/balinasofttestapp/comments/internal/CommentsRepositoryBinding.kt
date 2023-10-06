package com.globa.balinasofttestapp.comments.internal

import com.globa.balinasofttestapp.comments.api.CommentsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
internal interface CommentsRepositoryBinding {
    @Binds
    fun bindCommentsRepository(repositoryImpl: CommentsRepositoryImpl): CommentsRepository
}