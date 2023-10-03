package com.globa.balinasofttestapp.location.internal

import android.annotation.SuppressLint
import com.globa.balinasofttestapp.common.di.IoDispatcher
import com.globa.balinasofttestapp.location.api.LocationResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataSource @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) {
    private val cancellationToken = object:  CancellationToken() {
        override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token
        override fun isCancellationRequested() = false
    }
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LocationResponse = withContext(coroutineDispatcher) {
        val task = fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken)
        val result = Tasks.await(task)
        if (result != null) LocationResponse.Success(result)
        else LocationResponse.Error("Location error")
    }
}