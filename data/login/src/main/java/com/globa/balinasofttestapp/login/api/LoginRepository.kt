package com.globa.balinasofttestapp.login.api

import kotlinx.coroutines.flow.Flow


interface LoginRepository {
    fun getLoginStatus(): Flow<LoginStatus>
    suspend fun signUp(login: String, password: String)
    suspend fun signIn(login: String, password: String)
}