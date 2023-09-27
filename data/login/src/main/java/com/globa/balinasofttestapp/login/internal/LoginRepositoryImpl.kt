package com.globa.balinasofttestapp.login.internal

import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.login.api.LoginStatus
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.network.api.model.Resource
import com.globa.balinasofttestapp.network.api.model.SignUserDtoOut
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LoginRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val tokenNetworkDataSource: TokenNetworkDataSource,
    private val scope: CoroutineScope
): LoginRepository {
    private val _loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.NotAuthorised)

    init {
        scope.launch {
            tokenDataStore.getAccessToken().collect {token ->
                if (token.isNotEmpty()) _loginStatus.update { LoginStatus.Success(token = token) }
            }
        }
    }

    override fun getLoginStatus(): StateFlow<LoginStatus> = _loginStatus.asStateFlow()

    override suspend fun signUp(login: String, password: String) {
        handleSignResponse(tokenNetworkDataSource.signUp(login, password))
    }

    override suspend fun signIn(login: String, password: String) {
        handleSignResponse(tokenNetworkDataSource.signIn(login, password))
    }

    private fun handleSignResponse(response: NetworkResponse<Resource<SignUserDtoOut>>) {
        when (response) {
            is NetworkResponse.Success -> {
                _loginStatus.update {
                    LoginStatus.Success(response.data.body.token)
                }
            }
            is NetworkResponse.Error -> {
                _loginStatus.update {
                    LoginStatus.Error(response.message)
                }
            }
            is NetworkResponse.Exception -> {
                _loginStatus.update {
                    LoginStatus.Error(response.e.message?: "Unknown Error!")
                }
            }
        }
    }
}