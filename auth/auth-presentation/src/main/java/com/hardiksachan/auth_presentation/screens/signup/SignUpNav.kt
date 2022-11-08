package com.hardiksachan.auth_presentation.screens.signup

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hardiksachan.auth_application.AuthPresenter
import com.hardiksachan.auth_presentation.AuthFailureOrSuccessListener
import com.hardiksachan.auth_presentation.AuthViewModel

private const val SignUpRoutePattern = "signup"

internal fun NavGraphBuilder.signUpScreen(
    navigateToLoginPage: () -> Unit,
) {
    composable(SignUpRoutePattern) {
        val viewModel: AuthViewModel = hiltViewModel()
        val uiState = viewModel.state.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        AuthFailureOrSuccessListener(
            authFailureOrSuccessOption = uiState.value.authFailureOrSuccessOption,
            snackbarHostState = snackbarHostState
        )

        SignUpScreen(
            uiState = uiState.value,
            onEmailAddressChanged = { viewModel.handleEvent(AuthPresenter.Event.EmailChanged(it)) },
            onPasswordChanged = { viewModel.handleEvent(AuthPresenter.Event.PasswordChanged(it)) },
            onLoginButtonPressed = navigateToLoginPage,
            onSignUpButtonPressed = { viewModel.handleEvent(AuthPresenter.Event.RegisterWithEmailAndPasswordPressed) },
            onGoogleLoginPressed = { viewModel.handleEvent(AuthPresenter.Event.SignInWithGooglePressed) },
            snackbarHostState = snackbarHostState
        )
    }
}

fun NavController.navigateToSignUp() {
    navigate(SignUpRoutePattern) {
        launchSingleTop = true
    }
}