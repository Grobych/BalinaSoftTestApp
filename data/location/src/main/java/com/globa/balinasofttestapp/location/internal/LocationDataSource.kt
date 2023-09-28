package com.globa.balinasofttestapp.location.internal

import android.annotation.SuppressLint
import com.globa.balinasofttestapp.common.IoDispatcher
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
    suspend fun getCurrentLocation(): LocationResponse {
        lateinit var response: LocationResponse
        val task = fusedLocationProviderClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationToken)
            .addOnSuccessListener { response = LocationResponse.Success(it) }
            .addOnFailureListener { response = LocationResponse.Error(it.message?: "Unknown error") }
        withContext(coroutineDispatcher) { Tasks.await(task) }
        return response
    }
}