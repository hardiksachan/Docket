package com.hardiksachan.auth_presentation.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hardiksachan.auth_application.AuthPresenter
import com.hardiksachan.auth_presentation.components.SocialLogin
import com.hardiksachan.core_ui.theme.DocketTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignUpScreen(
    uiState: AuthPresenter.State,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSignUpButtonPressed: () -> Unit,
    onLoginButtonPressed: () -> Unit,
    onGoogleLoginPressed: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Sign Up", style = MaterialTheme.typography.displayMedium) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            SignUpForm(
                uiState = uiState,
                onEmailAddressChanged = onEmailAddressChanged,
                onPasswordChanged = onPasswordChanged
            )
            Spacer(modifier = Modifier.weight(1f))
            if (uiState.isSubmitting) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            SignUpFormButtons(onLoginButtonPressed, onSignUpButtonPressed)
            Spacer(modifier = Modifier.height(16.dp))
            SocialLogin(onGoogleLoginPressed)
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun SignUpFormButtons(
    onLoginButtonPressed: () -> Unit,
    onSignUpButtonPressed: () -> Unit
) {
    Button(
        onClick = onSignUpButtonPressed,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            "Sign Up",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    TextButton(
        onClick = onLoginButtonPressed,
    ) {
        Text(
            buildAnnotatedString {
                append("Already have an account? ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Log In")
                }
            },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Preview
@Composable
internal fun LogInScreenPreview() {
    DocketTheme {
        SignUpScreen(
            uiState = AuthPresenter.State(),
            onEmailAddressChanged = {},
            onPasswordChanged = {},
            onLoginButtonPressed = {},
            onSignUpButtonPressed = {},
            onGoogleLoginPressed = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}