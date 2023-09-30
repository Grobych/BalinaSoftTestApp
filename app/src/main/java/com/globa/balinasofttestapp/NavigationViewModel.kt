package com.globa.balinasofttestapp

import androidx.lifecycle.ViewModel
import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    loginRepository: LoginRepository
): ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val isAuthorized = loginRepository.getLoginStatus().mapLatest {
        if (it is AuthData.Success){
            it.token.isNotEmpty() && it.userName.isNotEmpty()
        } else false
    }
}