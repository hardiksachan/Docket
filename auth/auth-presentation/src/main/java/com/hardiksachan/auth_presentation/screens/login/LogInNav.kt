package com.hardiksachan.auth_presentation.screens.login

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hardiksachan.auth_application.AuthPresenter
import com.hardiksachan.auth_presentation.AuthViewModel
import com.hardiksachan.auth_presentation.toUserFriendlyMessage
import kotlinx.coroutines.launch

internal const val LogInRoutePattern = "login"

fun NavGraphBuilder.logInScreen(
    onNavigateToSignUpPage: () -> Unit
) {
    composable(LogInRoutePattern) {
        val viewModel: AuthViewModel = hiltViewModel()
        val uiState = viewModel.state.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        uiState.value.authFailureOrSuccessOption.fold(
            {},
            {
                it.fold(
                    { failure ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(failure.toUserFriendlyMessage())
                        }
                    },
                    { /*TODO: navigate to user page */ }
                )
            }
        )

        LogInScreen(
            uiState = uiState.value,
            onEmailAddressChanged = { viewModel.handleEvent(AuthPresenter.Event.EmailChanged(it)) },
            onPasswordChanged = { viewModel.handleEvent(AuthPresenter.Event.PasswordChanged(it)) },
            onLoginButtonPressed = { viewModel.handleEvent(AuthPresenter.Event.SignInWithEmailAndPasswordPressed) },
            onSignUpButtonPressed = onNavigateToSignUpPage,
            onGoogleLoginPressed = { viewModel.handleEvent(AuthPresenter.Event.SignInWithGooglePressed) },
            snackbarHostState = snackbarHostState
        )
    }
}

fun NavController.navigateToLogin() {
    navigate(LogInRoutePattern) {
        launchSingleTop = true
    }
}