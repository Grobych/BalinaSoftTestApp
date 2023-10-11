package com.globa.balinasofttestapp.login.internal

import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.network.api.model.Resource
import com.globa.balinasofttestapp.network.api.model.SignUserDtoOut
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

internal class LoginRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val userNameDataStore: UserNameDataStore,
    private val tokenNetworkDataSource: TokenNetworkDataSource
): LoginRepository {

    override fun getLoginStatus(): Flow<AuthData> = tokenDataStore.getAccessToken()
        .combine(userNameDataStore.getUserName()) { token,userName ->
            AuthData.Success(
                token = token,
                userName = userName
            )
        }

    override suspend fun signUp(login: String, password: String) =
        handleSignResponse(tokenNetworkDataSource.signUp(login, password))


    override suspend fun signIn(login: String, password: String) =
        handleSignResponse(tokenNetworkDataSource.signIn(login, password))

    override suspend fun logOut() {
        tokenDataStore.saveAccessToken("")
        userNameDataStore.saveUserName("")
    }


    private suspend fun handleSignResponse(response: NetworkResponse<Resource<SignUserDtoOut>>): AuthData {
        return when (response) {
            is NetworkResponse.Success -> {
                val login = response.data.body.login
                val token = response.data.body.token
                tokenDataStore.saveAccessToken(token)
                userNameDataStore.saveUserName(login)
                AuthData.Success(token = token, userName = login)
            }

            is NetworkResponse.Error -> {
                AuthData.Error(response.message)
            }

            is NetworkResponse.Exception -> {
                AuthData.Error(response.e.message?: "Unknown error")
            }
        }
    }
}