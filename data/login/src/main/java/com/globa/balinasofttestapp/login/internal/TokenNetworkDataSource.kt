package com.globa.balinasofttestapp.login.internal

import com.globa.balinasofttestapp.network.api.LoginNetworkApi
import com.globa.balinasofttestapp.network.api.model.SignUserDtoIn
import javax.inject.Inject

internal class TokenNetworkDataSource @Inject constructor(
    private val loginNetworkApi: LoginNetworkApi
) {
    suspend fun signUp(login: String, password: String) = loginNetworkApi.signUp(
        SignUserDtoIn(
            login = login,
            password = password
        )
    )
    suspend fun signIn(login: String, password: String) = loginNetworkApi.signIn(
        SignUserDtoIn(
            login = login,
            password = password
        )
    )
}