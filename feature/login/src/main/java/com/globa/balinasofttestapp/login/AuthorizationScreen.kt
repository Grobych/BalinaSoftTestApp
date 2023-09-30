package com.globa.balinasofttestapp.login

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.globa.balinasofttestapp.login.api.LoginStatus

@Composable
fun AuthorizationScreen(
    viewModel: AuthorizationViewModel = hiltViewModel(),
    navigateToMainScreen: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    val errorUiState = viewModel.errorUiState.collectAsState()
    val selectedTab = viewModel.tab.collectAsState()

    val loginStatus = viewModel.isAuthorized.collectAsState()
    if (loginStatus.value is LoginStatus.Success) navigateToMainScreen()
    
    val tabClicked = fun (tab: AuthorizationScreenTab) {viewModel.selectTab(tab)}
    val onReturnButtonClick = fun() {viewModel.onReturnClick()}
    val onLoginChanged = fun(login: String) {viewModel.loginChanged(login)}
    val onPasswordChanged = fun(password: String) {viewModel.passwordChanged(password)}
    val onSecondPasswordChanged = fun(password: String) {viewModel.secondPasswordChanged(password)}
    val onSignInButtonClicked = fun () {viewModel.signInClicked()}
    val onSignUpButtonClicked = fun () {viewModel.signUpClicked()}

    Scaffold(
        topBar = {
            AuthorizationScreenHeader(
                selectedTab = selectedTab.value,
                onTabClicked = tabClicked
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            when (val state = uiState.value) {
                is AuthorizationUiState.Sign -> {
                    when (selectedTab.value) {
                        AuthorizationScreenTab.SignIn -> {
                            SignInScreen(
                                login = state.login,
                                password = state.password,
                                error = errorUiState.value.errorMessage,
                                isLoginError = errorUiState.value.isLoginError,
                                isPasswordError = errorUiState.value.isPasswordError,
                                onLoginChanged = onLoginChanged,
                                onPasswordChanged = onPasswordChanged,
                                onSignInButtonClicked = onSignInButtonClicked
                            )
                        }
                        AuthorizationScreenTab.SignUp -> {
                            SignUpScreen(
                                login = state.login,
                                password = state.password,
                                confirmPassword = state.passwordToConfirm,
                                error = errorUiState.value.errorMessage,
                                isLoginError = errorUiState.value.isLoginError,
                                isPasswordError = errorUiState.value.isPasswordError,
                                isConfirmPasswordError = errorUiState.value.isConfirmPasswordError,
                                onLoginChanged = onLoginChanged,
                                onPasswordChanged = onPasswordChanged,
                                onConfirmPasswordChanged = onSecondPasswordChanged,
                                onSignUpButtonClicked = onSignUpButtonClicked
                            )
                        }
                    }
                }
                is AuthorizationUiState.Error -> {
                    AuthorizationError(
                        message = state.message,
                        returnAction = {onReturnButtonClick()}
                    )
                }
                is AuthorizationUiState.Sending -> {
                    SendingAnimation()
                }
            }
        }
    }
}

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    login: String,
    password: String,
    error: String?,
    isLoginError: Boolean,
    isPasswordError: Boolean,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignInButtonClicked: () -> Unit
) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = error?: "",
                color = Color.Red
            )
            Column {
                TextField(
                    value = login,
                    onValueChange = {onLoginChanged(it)},
                    singleLine = true,
                    isError = isLoginError,
                    label = { Text(text = "Login")}
                )
                TextField(
                    modifier = Modifier.padding(top = 20.dp),
                    value = password,
                    onValueChange = {onPasswordChanged(it)},
                    singleLine = true,
                    isError = isPasswordError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    label = { Text(text = "Password")},
                    visualTransformation = PasswordVisualTransformation()
                )
            }

            Button(
                onClick = { onSignInButtonClicked() },
            ) {
                Text("Sign In")
            }
        }
}

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    login: String,
    password: String,
    confirmPassword: String,
    error: String?,
    isLoginError: Boolean,
    isPasswordError: Boolean,
    isConfirmPasswordError: Boolean,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onSignUpButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = error ?: "",
            color = Color.Red
        )
        Column {
            TextField(
                value = login,
                onValueChange = { onLoginChanged(it) },
                singleLine = true,
                isError = isLoginError,
                label = { Text(text = "Login") }
            )
            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = password,
                onValueChange = { onPasswordChanged(it) },
                singleLine = true,
                isError = isPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            TextField(
                modifier = Modifier.padding(top = 20.dp),
                value = confirmPassword,
                onValueChange = { onConfirmPasswordChanged(it) },
                singleLine = true,
                isError = isConfirmPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                label = { Text(text = "Confirm password") },
                visualTransformation = PasswordVisualTransformation()
            )
        }

        Button(
            onClick = { onSignUpButtonClicked() },
        ) {
            Text("Sign Up")
        }
    }
}

@Composable
fun AuthorizationScreenHeader(
    selectedTab: AuthorizationScreenTab,
    onTabClicked: (AuthorizationScreenTab) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = Modifier.height(120.dp) //TODO: use constants
    ) {
        Tab(
            selected = selectedTab == AuthorizationScreenTab.SignIn,
            onClick = { onTabClicked(AuthorizationScreenTab.SignIn) },
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(text = "Sing In")
        }
        Tab(
            selected = selectedTab == AuthorizationScreenTab.SignUp,
            onClick = { onTabClicked(AuthorizationScreenTab.SignUp) },
            modifier = Modifier.fillMaxHeight()
        ) {
            Text(text = "Sing Up")
        }
    }
}

@Composable
fun AuthorizationError(
    modifier: Modifier = Modifier,
    message: String,
    returnAction: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = message)
            Button(onClick = { returnAction() }) {
                Text(text = "Return to login")
            }
        }
    }
}

//@Preview
//@Preview(uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun AuthorizationErrorPreview() {
//    MaterialTheme {
//        Surface(
//            modifier = Modifier.size(360.dp,480.dp)
//        ) {
//            AuthorizationError(message = "Test message.") {}
//        }
//    }
//}

@Composable
fun SendingAnimation(
    modifier: Modifier = Modifier
) {
    //TODO:add animation
}

//@Preview
//@Preview(uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun SendingAnimationPreview() {
//    MaterialTheme {
//        Surface(
//            modifier = Modifier.size(360.dp,480.dp)
//        ) {
//            SendingAnimation()
//        }
//    }
//}
//
//@Preview
//@Preview(uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun AuthorizationScreenPreview() {
//    class FakeRepo(): LoginRepository {
//        override fun getLoginStatus(): StateFlow<LoginStatus> = MutableStateFlow(LoginStatus.NotAuthorised).asStateFlow()
//
//        override suspend fun signUp(login: String, password: String) {}
//
//        override suspend fun signIn(login: String, password: String) {}
//    }
//    val repository = FakeRepo()
//    val viewModel = AuthorizationViewModel(repository)
//    MaterialTheme {
//        Surface {
//            AuthorizationScreen(
//                viewModel = viewModel
//            )
//        }
//    }
//}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignInScreenPreview() {
    MaterialTheme {
        Surface {
            SignInScreen(
                login = "test login",
                password = "test password",
                error = null,
                isLoginError = false,
                isPasswordError = false,
                onLoginChanged = {},
                onPasswordChanged = {},
                onSignInButtonClicked = {}
            )
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SignInScreenWithErrorPreview() {
    MaterialTheme {
        Surface {
            SignInScreen(
                login = "test login",
                password = "",
                error = "Some error in login data",
                isLoginError = true,
                isPasswordError = false,
                onLoginChanged = {},
                onPasswordChanged = {},
                onSignInButtonClicked = {}
            )
        }
    }
}