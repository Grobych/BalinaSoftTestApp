package com.globa.balinasofttesrapp.database.internal

import android.content.Context
import androidx.room.Room
import com.globa.balinasofttesrapp.database.api.PhotosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule{
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext applicationContext: Context) : PhotosDatabase =
        Room.databaseBuilder(
            applicationContext,
            PhotosDatabase::class.java,
            "photos")
            .build()
}