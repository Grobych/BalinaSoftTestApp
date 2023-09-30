package com.globa.balinasofttestapp.login.api

sealed class AuthData {
    data class Success(val token: String, val userName: String) : AuthData()
    data class Error(val message: String) : AuthData()
}
