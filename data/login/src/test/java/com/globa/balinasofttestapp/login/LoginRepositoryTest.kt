package com.globa.balinasofttestapp.login

import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.internal.LoginRepositoryImpl
import com.globa.balinasofttestapp.login.internal.TokenDataStore
import com.globa.balinasofttestapp.login.internal.TokenNetworkDataSource
import com.globa.balinasofttestapp.login.internal.UserNameDataStore
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryTest {
    private val tokenNetworkDataSource = mockk<TokenNetworkDataSource>()
    private val userNameDataStore = mockk<UserNameDataStore>()
    private val tokenDataStore = mockk<TokenDataStore>()

    @Test
    fun noSavedTokenTest() = runTest {
        coEvery { tokenDataStore.getAccessToken() } returns flow { emit("") }
        coEvery { userNameDataStore.getUserName() } returns flow { emit("") }
        val repository = LoginRepositoryImpl(
            tokenDataStore = tokenDataStore,
            userNameDataStore = userNameDataStore,
            tokenNetworkDataSource = tokenNetworkDataSource
        )
        val loginStatus = repository.getLoginStatus()
        assert(loginStatus.last() is AuthData.Success)
        assert((loginStatus.last() as AuthData.Success).token == "")
        assert((loginStatus.last() as AuthData.Success).userName == "")    }

    @Test
    fun savedTokenTest() = runTest {
        val testToken = "testToken"
        val testUserName = "testName"
        coEvery { tokenDataStore.getAccessToken() } returns flow { emit(testToken) }
        coEvery { userNameDataStore.getUserName() } returns flow { emit(testUserName) }
        val repository = LoginRepositoryImpl(
            tokenDataStore = tokenDataStore,
            userNameDataStore = userNameDataStore,
            tokenNetworkDataSource = tokenNetworkDataSource
        )
        val loginStatus = repository.getLoginStatus()
        advanceUntilIdle()
        assert(loginStatus.last() is AuthData.Success)
        assert((loginStatus.last() as AuthData.Success).token == testToken)
        assert((loginStatus.last() as AuthData.Success).userName == testUserName)
    }
}