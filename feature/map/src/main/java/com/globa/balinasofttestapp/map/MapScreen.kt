package com.globa.balinasofttestapp.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.globa.balinasofttestapp.camera.CreatePhotoFloatingButton
import com.globa.balinasofttestapp.common.ui.composable.MenuHeader
import com.globa.balinasofttestapp.common.ui.composable.permissions.LocationPermissions
import com.globa.balinasofttestapp.photos.api.model.PhotoLocation
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    drawerState: DrawerState,
    viewModel: MapViewModel = hiltViewModel(),
    onMarkerCLick: (Int) -> Unit,
    navigateToCamera: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            MenuHeader {
                coroutineScope.launch {
                    drawerState.open()
                }
            }
        },
        floatingActionButton = {
            CreatePhotoFloatingButton(navigateToCameraScreen = navigateToCamera)
        }
    ) {
        LocationPermissions {
            val state = viewModel.uiState.collectAsState()
            MapScreenContent(
                modifier = Modifier.padding(it),
                locations = state.value.photos,
                onMarkerCLick = onMarkerCLick
            )
        }
    }
}

@Composable
fun MapScreenContent(
    modifier: Modifier = Modifier,
    locations: List<PhotoLocation>,
    onMarkerCLick: (Int) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = MapProperties(isMyLocationEnabled = true),
        ) {
            locations.forEach {photoLocation ->
                Marker(
                    state = rememberMarkerState(position = LatLng(photoLocation.latitude,photoLocation.longitude)),
                    onClick = {
                        onMarkerCLick(photoLocation.id)
                        true
                    }
                )
            }
        }
    }
}