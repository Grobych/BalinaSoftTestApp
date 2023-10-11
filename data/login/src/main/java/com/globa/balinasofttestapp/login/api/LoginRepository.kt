package com.globa.balinasofttestapp.login.api

import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun getLoginStatus(): Flow<AuthData>
    suspend fun signUp(login: String, password: String): AuthData
    suspend fun signIn(login: String, password: String): AuthData
    suspend fun logOut()
}