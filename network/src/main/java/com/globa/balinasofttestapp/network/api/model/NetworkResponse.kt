package com.globa.balinasofttestapp.network.api.model

sealed class NetworkResponse<T : Any> {
    data class Success<T : Any>(val data: T): NetworkResponse<T>()
    data class Error<T: Any>(val code: Int, val message: String) : NetworkResponse<T>()
    data class Exception<T: Any>(val e: Throwable) : NetworkResponse<T>()
}