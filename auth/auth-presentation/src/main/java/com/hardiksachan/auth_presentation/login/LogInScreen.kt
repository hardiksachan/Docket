package com.hardiksachan.auth_presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hardiksachan.auth_domain.EmailAddress
import com.hardiksachan.auth_domain.Password
import com.hardiksachan.auth_presentation.SocialLogin
import com.hardiksachan.core_ui.theme.DocketTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LogInScreen(
    uiState: com.hardiksachan.auth_application.AuthPresenter.State,
    onEmailAddressChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginButtonPressed: () -> Unit,
    onSignUpButtonPressed: () -> Unit,
    onGoogleLoginPressed: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Log In", style = MaterialTheme.typography.displayMedium) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            LogInForm(
                uiState = uiState,
                onEmailAddressChanged = onEmailAddressChanged,
                onPasswordChanged = onPasswordChanged
            )
            Spacer(modifier = Modifier.weight(1f))
            LoginFormButtons(onLoginButtonPressed, onSignUpButtonPressed)
            Spacer(modifier = Modifier.height(16.dp))
            SocialLogin(onGoogleLoginPressed)
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun LoginFormButtons(
    onLoginButtonPressed: () -> Unit,
    onSignUpButtonPressed: () -> Unit
) {
    Button(
        onClick = onLoginButtonPressed,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            "Log In",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyLarge
        )
    }
    TextButton(
        onClick = onSignUpButtonPressed,
    ) {
        Text(
            buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Sign Up")
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
        LogInScreen(
            uiState = com.hardiksachan.auth_application.AuthPresenter.State(),
            onEmailAddressChanged = {},
            onPasswordChanged = {},
            onLoginButtonPressed = {},
            onSignUpButtonPressed = {},
            onGoogleLoginPressed = {}
        )
    }
}

@Preview
@Composable
internal fun LogInScreenPreviewWithError() {
    DocketTheme {
        LogInScreen(
            uiState = com.hardiksachan.auth_application.AuthPresenter.State(
                email = EmailAddress.create("ab"),
                password = Password.create("dgf"),
                showErrorMessages = true,
            ),
            onEmailAddressChanged = {},
            onPasswordChanged = {},
            onLoginButtonPressed = {},
            onSignUpButtonPressed = {},
            onGoogleLoginPressed = {}
        )
    }
}