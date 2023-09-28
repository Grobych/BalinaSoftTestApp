package com.globa.balinasofttestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globa.balinasofttestapp.location.api.LocationRepository
import com.globa.balinasofttestapp.location.api.LocationResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestLocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel() {
    private val _locationState = MutableStateFlow<LocationResponse>(LocationResponse.Unset)
    val locationState = _locationState.asStateFlow()

    fun flushLocation() {
        viewModelScope.launch {
            _locationState.update { locationRepository.getLocation() }
        }
    }
}