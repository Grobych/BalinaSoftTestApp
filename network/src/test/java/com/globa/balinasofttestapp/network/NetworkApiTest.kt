package com.globa.balinasofttestapp.network

import com.globa.balinasofttestapp.network.api.model.NetworkResponse
import com.globa.balinasofttestapp.network.api.model.SignUserDtoIn
import com.globa.balinasofttestapp.network.internal.NetworkModule
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkApiTest {
    private val testLogin = "globa"
    private val testPassword = "123123123"
    private val testToken = "3b0m6h3CLcvSo5fKzb9PLMWtYMJlCdgFeMJLzidKOJlbtMrwn6PKoN22v9Au1kBR"
    private val retrofit = NetworkModule().provideRetrofit()
    private val loginApi = NetworkModule().provideLoginNetworkApi(retrofit)
    private val imageApi = NetworkModule().provideImageNetworkApi(retrofit)

    @Test
    fun loginTest() = runTest {
        val response = loginApi.signIn(SignUserDtoIn(login = testLogin, password = testPassword))
        assert(response is NetworkResponse.Success)
        println((response as NetworkResponse.Success).data.body.token)
        assert(response.data.body.token.isNotEmpty())
    }

    @Test
    fun getImagesTest() = runTest {
        val getImageResponse = imageApi.getImages(token = testToken, page = 1)
        println(getImageResponse)
        assert(getImageResponse is NetworkResponse.Success)
    }

    @Test
    fun reuseLoginTryingSignUpTest() = runTest {
        val errorMessage = "security.signup.login-already-use"
        val response = loginApi.signUp(
            SignUserDtoIn(
                login = testLogin,
                password = testPassword
            )
        )
        assert(response is NetworkResponse.Error)
        assert((response as NetworkResponse.Error).message == errorMessage)
    }
}