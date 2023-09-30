package com.globa.balinasofttestapp.login

sealed class AuthorizationUiState{
    data class Error(val message: String): AuthorizationUiState()
    object Sending: AuthorizationUiState()
    data class Sign(
        val login: String = "",
        val password: String = "",
        val passwordToConfirm: String = ""
    ): AuthorizationUiState()
}

data class AuthorizationErrorUiState(
    val isLoginError: Boolean = false,
    val isPasswordError:Boolean = false,
    val isConfirmPasswordError: Boolean = false,
    val errorMessage: String = ""
)

enum class AuthorizationScreenTab { SignIn, SignUp}
