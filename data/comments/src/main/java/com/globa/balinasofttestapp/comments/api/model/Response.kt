package com.globa.balinasofttestapp.comments.api.model

sealed class Response<T : Any> {
    data class Success<T : Any>(val data: T): Response<T>()
    data class Error<T: Any>(val message: String) : Response<T>()
}