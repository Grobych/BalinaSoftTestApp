package com.globa.balinasofttestapp.login.internal

import com.globa.balinasofttestapp.login.api.LoginRepository
import com.globa.balinasofttestapp.login.api.LoginStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class LoginRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore,
    private val tokenNetworkDataSource: TokenNetworkDataSource
): LoginRepository {

    private val loginStatus = MutableStateFlow<LoginStatus>(LoginStatus.NotAuthorised)
    override fun getLoginStatus(): Flow<LoginStatus> =
        loginStatus.combine(tokenDataStore.getAccessToken()) { currentStatus, storeToken ->
            if (storeToken.isEmpty()) currentStatus
            else LoginStatus.Success(storeToken)
        }

    override suspend fun signUp(login: String, password: String) {
        try {
            val result = tokenNetworkDataSource.signUp(login, password)
            if (result.status == 200) tokenDataStore.saveAccessToken(result.data.token)
        } catch (e: Exception) {
            loginStatus.update { LoginStatus.Error(e.message?: "Unknown Error!") }
        }
    }

    override suspend fun signIn(login: String, password: String) {
        try {
            val result = tokenNetworkDataSource.signIn(login, password)
            if (result.status == 200) tokenDataStore.saveAccessToken(result.data.token)
        } catch (e: Exception) {
            loginStatus.update { LoginStatus.Error(e.message?: "Unknown Error!") }
        }
    }

}