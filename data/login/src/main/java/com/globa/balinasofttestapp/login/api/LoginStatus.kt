package com.globa.balinasofttestapp.login.api

sealed class LoginStatus {
    data class Success(val token: String): LoginStatus()
    object NotAuthorised: LoginStatus()
    data class Error(val message: String): LoginStatus()
}
