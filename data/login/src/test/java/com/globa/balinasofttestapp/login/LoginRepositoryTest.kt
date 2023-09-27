package com.globa.balinasofttestapp.login

import com.globa.balinasofttestapp.login.api.LoginStatus
import com.globa.balinasofttestapp.login.internal.LoginRepositoryImpl
import com.globa.balinasofttestapp.login.internal.TokenDataStore
import com.globa.balinasofttestapp.login.internal.TokenNetworkDataSource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepositoryTest {
    private val tokenNetworkDataSource = mockk<TokenNetworkDataSource>()
    private val tokenDataStore = mockk<TokenDataStore>()

    @Test
    fun noSavedTokenTest() = runTest {
        coEvery { tokenDataStore.getAccessToken() } returns flow { emit("") }
        val repository = LoginRepositoryImpl(
            tokenDataStore = tokenDataStore,
            tokenNetworkDataSource = tokenNetworkDataSource,
            scope = this
        )
        val loginStatus = repository.getLoginStatus()
        assert(loginStatus.value == LoginStatus.NotAuthorised)
    }

    @Test
    fun savedTokenTest() = runTest {
        val testToken = "testToken"
        coEvery { tokenDataStore.getAccessToken() } returns flow { emit(testToken) }
        val repository = LoginRepositoryImpl(
            tokenDataStore = tokenDataStore,
            tokenNetworkDataSource = tokenNetworkDataSource,
            scope = this
        )
        val loginStatus = repository.getLoginStatus()
        advanceUntilIdle()
        assert(loginStatus.value is LoginStatus.Success)
        assert((loginStatus.value as LoginStatus.Success).token == testToken)
    }
}