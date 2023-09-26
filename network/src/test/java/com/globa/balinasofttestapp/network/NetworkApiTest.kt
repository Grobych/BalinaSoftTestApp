package com.globa.balinasofttestapp.network

import com.globa.balinasofttestapp.network.api.model.SignUserDtoIn
import com.globa.balinasofttestapp.network.internal.NetworkModule
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkApiTest {
    private val testLogin = "globa"
    private val testPassword = "123123123"
    private val retrofit = NetworkModule().provideRetrofit()
    private val loginApi = NetworkModule().provideLoginNetworkApi(retrofit)
    private val imageApi = NetworkModule().provideImageNetworkApi(retrofit)

    @Test
    fun loginTest() = runTest {
        val response = loginApi.signIn(SignUserDtoIn(login = testLogin, password = testPassword))
        assert(response.status == 200)
        assert(response.data.token.isNotEmpty())
        assert(response.data.login == testLogin)
    }

    @Test
    fun getImagesTest() = runTest {
        val loginResponse = loginApi.signIn(SignUserDtoIn(login = testLogin, password = testPassword))
        assert(loginResponse.status == 200)
        val token = loginResponse.data.token
        val getImageResponse = imageApi.getImages(token = token, page = 1)
        assert(getImageResponse.status == 200)
    }
}