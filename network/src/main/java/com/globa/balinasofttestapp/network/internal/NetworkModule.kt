package com.globa.balinasofttestapp.network.internal

import com.globa.balinasofttestapp.network.api.CommentsNetworkApi
import com.globa.balinasofttestapp.network.api.ImagesNetworkApi
import com.globa.balinasofttestapp.network.api.LoginNetworkApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideLoginNetworkApi(retrofit: Retrofit): LoginNetworkApi =
        retrofit.create(LoginNetworkApi::class.java)

    @Provides
    @Singleton
    fun provideImageNetworkApi(retrofit: Retrofit): ImagesNetworkApi =
        retrofit.create(ImagesNetworkApi::class.java)

    @Provides
    @Singleton
    fun provideCommentsNetworkApi(retrofit: Retrofit): CommentsNetworkApi =
        retrofit.create(CommentsNetworkApi::class.java)

}