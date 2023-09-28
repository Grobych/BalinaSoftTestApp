package com.globa.balinasofttestapp.login.api

sealed class LoginStatus {
    data class Success(val authData: AuthData): LoginStatus()
    object NotAuthorised: LoginStatus()
    data class Error(val message: String): LoginStatus()
}

data class AuthData(
    val token: String,
    val userName: String
)
