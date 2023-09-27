package com.globa.balinasofttestapp.login.api

import kotlinx.coroutines.flow.StateFlow


interface LoginRepository {
    fun getLoginStatus(): StateFlow<LoginStatus>
    suspend fun signUp(login: String, password: String)
    suspend fun signIn(login: String, password: String)
}