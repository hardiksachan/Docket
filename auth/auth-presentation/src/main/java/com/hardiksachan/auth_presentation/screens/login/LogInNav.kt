package com.hardiksachan.auth_presentation.screens.login

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hardiksachan.auth_application.AuthPresenter
import com.hardiksachan.auth_presentation.AuthViewModel

internal const val LogInRoutePattern = "login"

fun NavGraphBuilder.logInScreen(
    onNavigateToSignUpPage : () -> Unit
) {
    composable(LogInRoutePattern) {
        val viewModel: AuthViewModel = hiltViewModel()
        val uiState = viewModel.state.collectAsState()
        LogInScreen(
            uiState = uiState.value,
            onEmailAddressChanged = { viewModel.handleEvent(AuthPresenter.Event.EmailChanged(it)) },
            onPasswordChanged = { viewModel.handleEvent(AuthPresenter.Event.PasswordChanged(it)) },
            onLoginButtonPressed = { viewModel.handleEvent(AuthPresenter.Event.SignInWithEmailAndPasswordPressed) },
            onSignUpButtonPressed = onNavigateToSignUpPage,
            onGoogleLoginPressed = { viewModel.handleEvent(AuthPresenter.Event.SignInWithGooglePressed) },
        )
    }
}

fun NavController.navigateToLogin() {
    navigate(LogInRoutePattern) {
        launchSingleTop = true
    }
}