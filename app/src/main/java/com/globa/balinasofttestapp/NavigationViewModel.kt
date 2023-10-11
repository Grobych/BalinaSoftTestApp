package com.globa.balinasofttestapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.photos.api.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val photosRepository: PhotosRepository
): ViewModel() {
    fun logout() {
        viewModelScope.launch {
            loginRepository.logOut()
            photosRepository.removeAllLocalPhotos()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userName = loginRepository.getLoginStatus().mapLatest {
        if (it is AuthData.Success) {
            it.userName
        } else ""
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    val isAuthorized = loginRepository.getLoginStatus().mapLatest {
        if (it is AuthData.Success){
            it.token.isNotEmpty() && it.userName.isNotEmpty()
        } else false
    }
}