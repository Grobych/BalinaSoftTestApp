package com.globa.balinasofttestapp.login.internal

import com.globa.balinasofttestapp.login.api.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface LoginRepositoryBinding {
    @Binds
    fun bindLoginRepository(repositoryImpl: LoginRepositoryImpl): LoginRepository
}