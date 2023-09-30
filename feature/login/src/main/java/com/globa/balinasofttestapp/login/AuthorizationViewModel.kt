package com.globa.balinasofttestapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globa.balinasofttestapp.login.api.AuthData
import com.globa.balinasofttestapp.login.api.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val repository: LoginRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<AuthorizationUiState>(AuthorizationUiState.Sign())
    val uiState = _uiState.asStateFlow()

    private val _errorUiState = MutableStateFlow(AuthorizationErrorUiState())
    val errorUiState = _errorUiState.asStateFlow()

    private val _tab = MutableStateFlow(AuthorizationScreenTab.SignIn)
    val tab = _tab.asStateFlow()

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized = _isAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLoginStatus()
                .onEach {
                    when(val data = it) {
                        is AuthData.Success -> {
                            if (data.token.isNotEmpty() && data.userName.isNotEmpty())
                                _isAuthorized.value = true
                        }
                        is AuthData.Error -> {
                            _uiState.update { AuthorizationUiState.Error(data.message) }
                        }
                    }
                }
        }
    }

    fun selectTab(tab: AuthorizationScreenTab) = _tab.update { tab }
    fun onReturnClick() {
        _uiState.update { AuthorizationUiState.Sign() }
    }

    fun loginChanged(login: String) {
        if (_uiState.value is AuthorizationUiState.Sign) {
            val current = _uiState.value as AuthorizationUiState.Sign
            _uiState.update {
                current.copy(
                    login = login
                )
            }
        }
    }

    fun passwordChanged(password: String) {
        if (_uiState.value is AuthorizationUiState.Sign) {
            val current = _uiState.value as AuthorizationUiState.Sign
            _uiState.update {
                current.copy(
                    password = password
                )
            }
        }
    }

    fun secondPasswordChanged(password: String) {
        if (_uiState.value is AuthorizationUiState.Sign) {
            val current = _uiState.value as AuthorizationUiState.Sign
            _uiState.update {
                current.copy(
                    passwordToConfirm = password
                )
            }
        }
    }

    private fun checkInput(state: AuthorizationUiState.Sign, tab: AuthorizationScreenTab): AuthorizationErrorUiState {
        if (state.login.length < 4 || state.login.length > 32)
            return AuthorizationErrorUiState(
                isLoginError = true,
                errorMessage = "The login must be a minimum of 4 and a maximum of 32 characters in length"
            )
        if (!Regex("[a-z0-9_\\-.@]+").matches(state.login))
            return AuthorizationErrorUiState(
                isLoginError = true,
                errorMessage = "Login must contain only Latin alphabet characters, numbers and some special characters: _\\-.@]+ "
            )
        if (state.password.length < 8 || state.password.length > 500)
            return AuthorizationErrorUiState(
                isPasswordError = true,
                errorMessage = "The password must be a minimum of 8 and a maximum of 500 characters in length "
            )
        if (tab == AuthorizationScreenTab.SignUp && state.password != state.passwordToConfirm)
            return AuthorizationErrorUiState(
                isConfirmPasswordError = true,
                errorMessage = "Passwords are not equal"
            )
        return AuthorizationErrorUiState()
    }

    fun signInClicked() {
        val state = uiState.value
        if (state is AuthorizationUiState.Sign) {
            _errorUiState.update {
                checkInput(state,tab.value)
            }
            val errorUiState = errorUiState.value
            if (!errorUiState.isLoginError &&
                !errorUiState.isPasswordError
            ) {
                viewModelScope.launch {
                    when(val response = repository.signIn(state.login,state.password)) {
                        is AuthData.Error -> {
                            _uiState.update {
                                AuthorizationUiState.Error(response.message)
                            }
                        }
                        is AuthData.Success -> {
                            _isAuthorized.value = true
                        }
                    }
                }
            }
        }
    }

    fun signUpClicked() {
        val state = uiState.value
        if (state is AuthorizationUiState.Sign) {
            _errorUiState.update {
                checkInput(state,tab.value)
            }
            val errorUiState = errorUiState.value
            if (!errorUiState.isLoginError &&
                !errorUiState.isPasswordError &&
                !errorUiState.isConfirmPasswordError
            ) {
                viewModelScope.launch {
                    when(val response = repository.signUp(state.login,state.password)) {
                        is AuthData.Error -> {
                            _uiState.update {
                                AuthorizationUiState.Error(response.message)
                            }
                        }
                        is AuthData.Success -> {
                            _isAuthorized.value = true
                        }
                    }
                }
            }
        }
    }
}